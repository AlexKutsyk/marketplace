package com.otus.otuskotlin.skillGrader.repo.pgsqlx4k

import com.benasher44.uuid.uuid4
import com.otus.otuskotlin.skillGrader.project.common.helpers.errorSystem
import com.otus.otuskotlin.skillGrader.project.common.models.AppGrade
import com.otus.otuskotlin.skillGrader.project.common.models.AppRule
import com.otus.otuskotlin.skillGrader.project.common.models.AppRuleId
import com.otus.otuskotlin.skillGrader.project.common.models.AppRuleLock
import com.otus.otuskotlin.skillGrader.project.common.repo.DbRuleFilterRequest
import com.otus.otuskotlin.skillGrader.project.common.repo.DbRuleIdRequest
import com.otus.otuskotlin.skillGrader.project.common.repo.DbRuleRequest
import com.otus.otuskotlin.skillGrader.project.common.repo.IDbRuleResponse
import com.otus.otuskotlin.skillGrader.project.common.repo.IDbRulesResponse
import com.otus.otuskotlin.skillGrader.project.common.repo.IRepoRule
import com.otus.otuskotlin.skillGrader.project.common.repo.errorDb
import com.otus.otuskotlin.skillGrader.project.common.repo.errorEmptyId
import com.otus.otuskotlin.skillGrader.project.common.repo.errorEmptyLock
import com.otus.otuskotlin.skillGrader.project.common.repo.errorNotFound
import com.otus.otuskotlin.skillGrader.project.common.repo.errorRepoConcurrency
import com.otus.otuskotlin.skillGrader.project.common.repo.exceptions.RepoEmptyLockException
import com.otus.otuskotlin.skillGrader.repo.common.IRepoRuleInitializable
import io.github.smyrgeorge.sqlx4k.ConnectionPool
import io.github.smyrgeorge.sqlx4k.Statement
import io.github.smyrgeorge.sqlx4k.postgres.postgreSQL
import kotlinx.coroutines.runBlocking

class RepoRuleSql(
    properties: SqlProperties = SqlProperties(),
    private val randomUuid: () -> String = { uuid4().toString() },
) : IRepoRule, IRepoRuleInitializable {

    private val db by lazy {
        postgreSQL(
            url = properties.url,
            username = properties.user,
            password = properties.password,
            options = ConnectionPool.Options(maxConnections = properties.maxConnections),
        )
    }

    private val dbName = "\"${properties.schema}\".\"${properties.table}\""
    private val cols = SqlFields.allFields.joinToString { it.quoted() }

    override suspend fun createRule(request: DbRuleRequest): IDbRuleResponse = tryRuleMethod {
        val ad = request.rule.copy(id = AppRuleId(randomUuid()), lock = AppRuleLock(randomUuid()))
        val stmt = Statement.create(SqlQueryBuilder.insert(dbName, cols)).bindRule(ad)
        val rows: List<AppRule> = db.fetchAll(stmt, RuleRowMapper).getOrThrow()
        if (rows.isEmpty()) throw RuntimeException("DB error: insert returned no rows")
        IDbRuleResponse.DbRuleSuccessResponse(rows.first())
    }

    override suspend fun readRule(request: DbRuleIdRequest): IDbRuleResponse = tryRuleMethod {
        val stmt = Statement.create(SqlQueryBuilder.read(dbName, cols))
            .bind(SqlFields.ID, request.id.asString())
        val rows: List<AppRule> = db.fetchAll(stmt, RuleRowMapper).getOrThrow()
        if (rows.isEmpty()) errorNotFound(request.id) else IDbRuleResponse.DbRuleSuccessResponse(rows.first())
    }

    override suspend fun updateRule(request: DbRuleRequest): IDbRuleResponse = tryRuleMethod {
        val newRule = request.rule.copy(lock = AppRuleLock(randomUuid()))
        val stmt = Statement.create(SqlQueryBuilder.update(dbName, cols))
            .bindRule(newRule)
            .bind(SqlFields.LOCK_OLD, request.rule.lock.asString())
        val rows: List<AppRule> = db.fetchAll(stmt, RuleRowMapper).getOrThrow()
        val returnedAd = rows.firstOrNull()
        when {
            returnedAd == null -> errorNotFound(request.rule.id)
            returnedAd.lock == newRule.lock -> IDbRuleResponse.DbRuleSuccessResponse(returnedAd)
            else -> errorRepoConcurrency(returnedAd, request.rule.lock)
        }
    }

    override suspend fun deleteRule(request: DbRuleIdRequest): IDbRuleResponse = tryRuleMethod {
        val id = request.id.takeIf { it != AppRuleId.NONE } ?: return@tryRuleMethod errorEmptyId
        val oldLock = request.lock.takeIf { it != AppRuleLock.NONE } ?: return@tryRuleMethod errorEmptyLock(id)

        // 1. Читаем текущее состояние
        val current = fetchById(id)
        when {
            current == null -> errorNotFound(id)
            current.lock == AppRuleLock.NONE -> errorDb(RepoEmptyLockException(id))
            current.lock != oldLock -> errorRepoConcurrency(current, oldLock)
            else -> {
                // 2. Удаляем с проверкой lock
//                val stmt = Statement.create("DELETE FROM $dbName WHERE ${SqlFields.ID.quoted()} = :id AND ${SqlFields.LOCK.quoted()} = :lock")
//                    .bind("id", id.asString())
//                    .bind("lock", oldLock.asString())
                val stmt = Statement
                    .create(SqlQueryBuilder.delete(dbName, cols))
                    .bind(SqlFields.ID, id.asString())
                    .bind(SqlFields.LOCK_OLD, oldLock.asString())
                db.execute(stmt).getOrThrow()
                IDbRuleResponse.DbRuleSuccessResponse(current)
            }
        }
    }

    override suspend fun searchRule(request: DbRuleFilterRequest): IDbRulesResponse = tryRulesMethod {
        val sql = SqlQueryBuilder.search(
            dbName, cols,
            grade = request.grade != AppGrade.NONE,
            nameFilter = request.nameFilter.isNotBlank(),
        )
        val stmt = Statement.create(sql)
        if (request.grade != AppGrade.NONE)
            stmt.bind(SqlFields.GRADE, request.grade.toDbString())
        if (request.nameFilter.isNotBlank())
            stmt.bind(SqlFields.NAME, "%${request.nameFilter}%")
        val rows: List<AppRule> = db.fetchAll(stmt, RuleRowMapper).getOrThrow()
        IDbRulesResponse.DbRulesSuccessResponse(data = rows)
    }

    override fun save(ads: Collection<AppRule>): Collection<AppRule> = runBlocking {
        ads.map { ad ->
            val stmt = Statement.create(SqlQueryBuilder.insert(dbName, cols)).bindRule(ad)
            val rows: List<AppRule> = db.fetchAll(stmt, RuleRowMapper).getOrThrow()
            rows.first()
        }
    }

    fun clear(): Unit = runBlocking {
        db.execute(Statement.create(SqlQueryBuilder.clear(dbName))).getOrThrow()
    }

    private suspend fun fetchById(id: AppRuleId): AppRule? {
//        val stmt = Statement.create("SELECT $cols FROM $dbName WHERE ${SqlFields.ID.quoted()} = :id")
//            .bind("id", id.asString())
        val stmt = Statement
            .create(SqlQueryBuilder.read(dbName, cols))
            .bind(SqlFields.ID, id.asString())
        val rows: List<AppRule> = db.fetchAll(stmt, RuleRowMapper).getOrThrow()
        return rows.firstOrNull()
    }

    private suspend fun tryRuleMethod(block: suspend () -> IDbRuleResponse): IDbRuleResponse = try {
        block()
    } catch (e: Throwable) {
        IDbRuleResponse.DbRuleErrorResponse(errorSystem("methodException", e = e))
    }

    private suspend fun tryRulesMethod(block: suspend () -> IDbRulesResponse): IDbRulesResponse = try {
        block()
    } catch (e: Throwable) {
        IDbRulesResponse.DbRulesErrorResponse(errorSystem("methodException", e = e))
    }
}