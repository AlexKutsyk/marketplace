package com.otus.otuskotlin.skillGrader.project.repo.tests

import com.otus.otuskotlin.skillGrader.project.common.models.AppGrade
import com.otus.otuskotlin.skillGrader.project.common.models.AppRule
import com.otus.otuskotlin.skillGrader.project.common.repo.DbRuleFilterRequest
import com.otus.otuskotlin.skillGrader.project.common.repo.IDbRulesResponse
import com.otus.otuskotlin.skillGrader.project.common.repo.IRepoRule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

abstract class RepoRuleSearchTest {
    abstract val repo: IRepoRule

    protected open val initializedObjects: List<AppRule> = initObjects

    @Test
    fun searchByName() = runRepoTest {
        val result = repo.searchRule(DbRuleFilterRequest(nameFilter = "A1"))
        assertIs<IDbRulesResponse.DbRulesSuccessResponse>(result)
        val expected = listOf(initializedObjects[1], initializedObjects[3]).sortedBy { it.id.asString() }
        assertEquals(expected, result.data.sortedBy { it.id.asString() })
    }

    @Test
    fun searchByGrade() = runRepoTest {
        val result = repo.searchRule(DbRuleFilterRequest(grade = AppGrade.A1))
        assertIs<IDbRulesResponse.DbRulesSuccessResponse>(result)
        val expected = listOf(initializedObjects[2], initializedObjects[4]).sortedBy { it.id.asString() }
        assertEquals(expected, result.data.sortedBy { it.id.asString() })
    }

    companion object: BaseInitRules("search") {
        override val initObjects: List<AppRule> = listOf(
            createInitTestModel("Rule 1"),
            createInitTestModel("Rule 2", grade = AppGrade.A2),
            createInitTestModel("Rule 3", grade = AppGrade.B1),
            createInitTestModel("Rule 4", grade = AppGrade.B2),
            createInitTestModel("Rule 5"),
        )
    }
}