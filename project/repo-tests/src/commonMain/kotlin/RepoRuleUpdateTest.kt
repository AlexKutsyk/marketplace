package com.otus.otuskotlin.skillGrader.project.repo.tests

import com.otus.otuskotlin.skillGrader.project.common.models.AppGrade
import com.otus.otuskotlin.skillGrader.project.common.models.AppRule
import com.otus.otuskotlin.skillGrader.project.common.models.AppRuleId
import com.otus.otuskotlin.skillGrader.project.common.models.AppRuleLock
import com.otus.otuskotlin.skillGrader.project.common.repo.DbRuleRequest
import com.otus.otuskotlin.skillGrader.project.common.repo.IDbRuleResponse
import com.otus.otuskotlin.skillGrader.project.common.repo.IRepoRule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

abstract class RepoRuleUpdateTest {
    abstract val repo: IRepoRule
    protected open val updateSuccess = initObjects[0]
    protected open val updateConcurrency = initObjects[1]
    protected val updateIdNotFound = AppRuleId("rule-repo-update-not-found")
    protected val lockBad = AppRuleLock("20000000-0000-0000-0000-000000000009")
    protected val lockNew = AppRuleLock("20000000-0000-0000-0000-000000000002")

    private val requestUpdateSuccess by lazy {
        AppRule(
            id = updateSuccess.id,
            name = "update object",
            grade = AppGrade.A1,
            minGrammarPercent = 5,
            minLexiconsPercent = 10,
            minListeningPercent = 15,
            timeWindowDays = 14,
            priority = 20,
            lock = initObjects.first().lock,
        )
    }

    private val reqUpdateNotFound = AppRule(
        id = updateIdNotFound,
        name = "update object not found",
        grade = AppGrade.A1,
        minGrammarPercent = 5,
        minLexiconsPercent = 10,
        minListeningPercent = 15,
        timeWindowDays = 14,
        priority = 20,
        lock = initObjects.first().lock,
    )

    private val requestUpdateConcurrency by lazy {
        AppRule(
            id = updateConcurrency.id,
            name = "update object not found",
            grade = AppGrade.A1,
            minGrammarPercent = 5,
            minLexiconsPercent = 10,
            minListeningPercent = 15,
            timeWindowDays = 14,
            priority = 20,
            lock = lockBad,
        )
    }

    @Test
    fun updateSuccess() = runRepoTest {
        val result = repo.updateRule(DbRuleRequest(requestUpdateSuccess))
        println("ERRORS: ${(result as? IDbRuleResponse.DbRuleErrorResponse)?.errors}")
        println("ERRORSWD: ${(result as? IDbRuleResponse.DbRuleErrorWithDataResponse)?.errors}")
        assertIs<IDbRuleResponse.DbRuleSuccessResponse>(result)
        assertEquals(requestUpdateSuccess.id, result.data.id)
        assertEquals(requestUpdateSuccess.name, result.data.name)
        assertEquals(requestUpdateSuccess.grade, result.data.grade)
        assertEquals(lockNew, result.data.lock)
    }

    @Test
    fun updateNotFound() = runRepoTest {
        val result = repo.updateRule(DbRuleRequest(reqUpdateNotFound))
        assertIs<IDbRuleResponse.DbRuleErrorResponse>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)
    }

    @Test
    fun updateConcurrencyError() = runRepoTest {
        val result = repo.updateRule(DbRuleRequest(requestUpdateConcurrency))
        assertIs<IDbRuleResponse.DbRuleErrorWithDataResponse>(result)
        val error = result.errors.find { it.code == "repo-concurrency" }
        assertEquals("lock", error?.field)
        assertEquals(updateConcurrency, result.data)
    }

    companion object : BaseInitRules("update") {
        override val initObjects: List<AppRule> = listOf(
            createInitTestModel("update"),
            createInitTestModel("updateConcurrency"),
        )
    }
}