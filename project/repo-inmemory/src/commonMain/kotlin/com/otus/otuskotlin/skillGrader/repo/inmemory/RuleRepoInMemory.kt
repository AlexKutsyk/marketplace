package com.otus.otuskotlin.skillGrader.repo.inmemory

import com.benasher44.uuid.uuid4
import com.otus.otuskotlin.skillGrader.project.common.models.AppGrade
import com.otus.otuskotlin.skillGrader.project.common.models.AppRule
import com.otus.otuskotlin.skillGrader.project.common.models.AppRuleId
import com.otus.otuskotlin.skillGrader.project.common.models.AppRuleLock
import com.otus.otuskotlin.skillGrader.project.common.repo.DbRuleFilterRequest
import com.otus.otuskotlin.skillGrader.project.common.repo.DbRuleIdRequest
import com.otus.otuskotlin.skillGrader.project.common.repo.DbRuleRequest
import com.otus.otuskotlin.skillGrader.project.common.repo.IDbRuleResponse
import com.otus.otuskotlin.skillGrader.project.common.repo.IDbRulesResponse
import com.otus.otuskotlin.skillGrader.project.common.repo.RuleRepoBase
import com.otus.otuskotlin.skillGrader.project.common.repo.errorDb
import com.otus.otuskotlin.skillGrader.project.common.repo.errorEmptyId
import com.otus.otuskotlin.skillGrader.project.common.repo.errorEmptyLock
import com.otus.otuskotlin.skillGrader.project.common.repo.errorNotFound
import com.otus.otuskotlin.skillGrader.project.common.repo.errorRepoConcurrency
import com.otus.otuskotlin.skillGrader.project.common.repo.exceptions.RepoEmptyLockException
import com.otus.otuskotlin.skillGrader.repo.common.IRepoRuleInitializable
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class RuleRepoInMemory(
    ttl: Duration = 2.minutes,
    val randomUuid: () -> String = { uuid4().toString() },
) : RuleRepoBase(), IRepoRuleInitializable {

    private val mutex: Mutex = Mutex()
    private val cache = Cache.Builder<String, RuleEntity>()
        .expireAfterWrite(ttl)
        .build()

    override fun save(ads: Collection<AppRule>) = ads.map { rule ->
        val entity = RuleEntity(rule)
        require(entity.id != null)
        cache.put(entity.id, entity)
        rule
    }

    override suspend fun createRule(request: DbRuleRequest): IDbRuleResponse = tryRuleMethod {
        val key = randomUuid()
        val ad = request.rule.copy(id = AppRuleId(key), lock = AppRuleLock(randomUuid()))
        val entity = RuleEntity(ad)
        mutex.withLock {
            cache.put(key, entity)
        }
        IDbRuleResponse.DbRuleSuccessResponse(ad)
    }

    override suspend fun readRule(request: DbRuleIdRequest): IDbRuleResponse = tryRuleMethod {
        val key = request.id.takeIf { it != AppRuleId.NONE }?.asString() ?: return@tryRuleMethod errorEmptyId
        mutex.withLock {
            cache.get(key)
                ?.let {
                    IDbRuleResponse.DbRuleSuccessResponse(it.toInternal())
                } ?: errorNotFound(request.id)
        }
    }

    override suspend fun updateRule(request: DbRuleRequest): IDbRuleResponse = tryRuleMethod {
        val requestRule = request.rule
        val id = requestRule.id.takeIf { it != AppRuleId.NONE } ?: return@tryRuleMethod errorEmptyId
        val key = id.asString()
        val oldLock = requestRule.lock.takeIf { it != AppRuleLock.NONE } ?: return@tryRuleMethod errorEmptyLock(id)

        mutex.withLock {
            val oldRule = cache.get(key)?.toInternal()
            when {
                oldRule == null -> errorNotFound(id)
                oldRule.lock == AppRuleLock.NONE -> errorDb(RepoEmptyLockException(id))
                oldRule.lock != oldLock -> errorRepoConcurrency(oldRule, oldLock)
                else -> {
                    val newRule = requestRule.copy(lock = AppRuleLock(randomUuid()))
                    val entity = RuleEntity(newRule)
                    cache.put(key, entity)
                    IDbRuleResponse.DbRuleSuccessResponse(newRule)
                }
            }
        }
    }


    override suspend fun deleteRule(request: DbRuleIdRequest): IDbRuleResponse = tryRuleMethod {
        val id = request.id.takeIf { it != AppRuleId.NONE } ?: return@tryRuleMethod errorEmptyId
        val key = id.asString()
        val oldLock = request.lock.takeIf { it != AppRuleLock.NONE } ?: return@tryRuleMethod errorEmptyLock(id)

        mutex.withLock {
            val oldRule = cache.get(key)?.toInternal()
            when {
                oldRule == null -> errorNotFound(id)
                oldRule.lock == AppRuleLock.NONE -> errorDb(RepoEmptyLockException(id))
                oldRule.lock != oldLock -> errorRepoConcurrency(oldRule, oldLock)
                else -> {
                    cache.invalidate(key)
                    IDbRuleResponse.DbRuleSuccessResponse(oldRule)
                }
            }
        }
    }

    /**
     * Поиск правил по фильтру
     * Если в фильтре не установлен какой-либо из параметров - по нему фильтрация не идет
     */
    override suspend fun searchRule(request: DbRuleFilterRequest): IDbRulesResponse = tryRulesMethod {
        val result: List<AppRule> = cache.asMap().asSequence()
            .filter { entry ->
                request.grade.takeIf { it != AppGrade.NONE }?.let {
                    it.name == entry.value.grade
                } ?: true
            }
            .filter { entry ->
                request.nameFilter.takeIf { it.isNotBlank() }?.let {
                    entry.value.name?.contains(it) ?: false
                } ?: true
            }
            .map { it.value.toInternal() }
            .toList()
        IDbRulesResponse.DbRulesSuccessResponse(result)
    }
}