package com.otus.otuskotlin.skillGrader.project.repo.tests

import com.otus.otuskotlin.skillGrader.project.common.models.AppGrade
import com.otus.otuskotlin.skillGrader.project.common.models.AppRule
import com.otus.otuskotlin.skillGrader.project.common.models.AppRuleId
import com.otus.otuskotlin.skillGrader.project.common.repo.DbRuleRequest
import com.otus.otuskotlin.skillGrader.project.common.repo.IDbRuleResponse
import com.otus.otuskotlin.skillGrader.repo.common.IRepoRuleInitializable
import kotlin.test.*

abstract class RepoRuleCreateTest {
    abstract val repo: IRepoRuleInitializable
    protected open val uuidNew = AppRuleId("10000000-0000-0000-0000-000000000001")

    private val createObj = AppRule(
        name = "create object",
        grade = AppGrade.A1,
        minGrammarPercent = 5,
        minLexiconsPercent = 10,
        minListeningPercent = 15,
        timeWindowDays = 14,
        priority = 20,
    )

    @Test
    fun createSuccess() = runRepoTest {
        val result = repo.createRule(DbRuleRequest(createObj))
        val expected = createObj
        assertIs<IDbRuleResponse.DbRuleSuccessResponse>(result)
        assertEquals(uuidNew, result.data.id)
        assertEquals(uuidNew.asString(), result.data.lock.asString())
        assertEquals(expected.name, result.data.name)
        assertEquals(expected.grade, result.data.grade)
        assertEquals(expected.minGrammarPercent, result.data.minGrammarPercent)
        assertEquals(expected.minLexiconsPercent, result.data.minLexiconsPercent)
        assertEquals(expected.minListeningPercent, result.data.minListeningPercent)
        assertEquals(expected.timeWindowDays, result.data.timeWindowDays)
        assertEquals(expected.priority, result.data.priority)
    }

    companion object : BaseInitRules("create") {
        override val initObjects: List<AppRule> = emptyList()
    }
}
