package com.otus.otuskotlin.skillGrader.project.repo.tests

import com.otus.otuskotlin.skillGrader.project.common.models.AppRule
import com.otus.otuskotlin.skillGrader.project.common.models.AppRuleId
import com.otus.otuskotlin.skillGrader.project.common.repo.DbRuleIdRequest
import com.otus.otuskotlin.skillGrader.project.common.repo.IDbRuleResponse
import com.otus.otuskotlin.skillGrader.project.common.repo.IRepoRule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

abstract class RepoRuleDeleteTest {
    abstract val repo: IRepoRule
    protected open val deleteSuccess = initObjects[0]
    protected open val deleteConcurrency = initObjects[1]
    protected open val notFoundId = AppRuleId("rule-repo-delete-notFound")

    @Test
    fun deleteSuccess() = runRepoTest {
        val lockOld = deleteSuccess.lock
        val result = repo.deleteRule(DbRuleIdRequest(deleteSuccess.id, lock = lockOld))
        assertIs< IDbRuleResponse.DbRuleSuccessResponse>(result)
        assertEquals(deleteSuccess.name, result.data.name)
    }

    @Test
    fun deleteNotFound() = runRepoTest {
        val result = repo.readRule(DbRuleIdRequest(notFoundId, lock = lockOld))

        assertIs<IDbRuleResponse.DbRuleErrorResponse>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertNotNull(error)
    }

    @Test
    fun deleteConcurrency() = runRepoTest {
        val result = repo.deleteRule(DbRuleIdRequest(deleteConcurrency.id, lock = lockBad))

        assertIs<IDbRuleResponse.DbRuleErrorWithDataResponse>(result)
        val error = result.errors.find { it.code == "repo-concurrency" }
        assertNotNull(error)
    }

    companion object : BaseInitRules("delete") {
        override val initObjects: List<AppRule> = listOf(
            createInitTestModel("delete"),
            createInitTestModel("deleteLock"),
        )
    }
}