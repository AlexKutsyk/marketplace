package com.otus.otuskotlin.skillGrader.project.repo.tests

import com.otus.otuskotlin.skillGrader.project.common.models.AppError
import com.otus.otuskotlin.skillGrader.project.common.models.AppRule
import com.otus.otuskotlin.skillGrader.project.common.models.AppRuleId
import com.otus.otuskotlin.skillGrader.project.common.repo.DbRuleIdRequest
import com.otus.otuskotlin.skillGrader.project.common.repo.IDbRuleResponse
import com.otus.otuskotlin.skillGrader.project.common.repo.IRepoRule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

abstract class RepoRuleReadTest {
    abstract val repo: IRepoRule
    protected open val readSuccess = initObjects[0]

    @Test
    fun readSuccess() = runRepoTest {
        val result = repo.readRule(DbRuleIdRequest(readSuccess.id))

        assertIs<IDbRuleResponse.DbRuleSuccessResponse>(result)
        assertEquals(readSuccess, result.data)
    }

    @Test
    fun readNotFound() = runRepoTest {
        println("REQUESTING")
        val result = repo.readRule(DbRuleIdRequest(notFoundId))
        println("RESULT: $result")

        assertIs<IDbRuleResponse.DbRuleErrorResponse>(result)
        println("ERRORS: ${result.errors}")
        val error: AppError? = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitRules("read") {
        override val initObjects: List<AppRule> = listOf(
            createInitTestModel("read")
        )

        val notFoundId = AppRuleId("rule-repo-read-notFound")
    }
}