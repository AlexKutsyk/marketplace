package com.otus.otuskotlin.skillGrader.biz.stub

import com.otus.otuskotlin.skillGrader.project.biz.RuleProcessor
import com.otus.otuskotlin.skillGrader.project.common.AppContext
import com.otus.otuskotlin.skillGrader.project.common.models.AppCommand
import com.otus.otuskotlin.skillGrader.project.common.models.AppRule
import com.otus.otuskotlin.skillGrader.project.common.models.AppRuleFilter
import com.otus.otuskotlin.skillGrader.project.common.models.AppState
import com.otus.otuskotlin.skillGrader.project.common.models.AppWorkMode
import com.otus.otuskotlin.skillGrader.project.common.stubs.AppStubs
import com.otus.otuskotlin.skillGrader.project.stubs.AppRuleStub
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.test.fail

class RuleSearchStubTest {

    private val processor = RuleProcessor()
    val filter = AppRuleFilter(searchString = "Beginner")

    @Test
    fun search() = runTest {

        val context = AppContext(
            command = AppCommand.SEARCH,
            state = AppState.NONE,
            workMode = AppWorkMode.STUB,
            stubCase = AppStubs.SUCCESS,
            ruleFilterRequest = filter,
        )
        processor.execute(context)
        assertTrue(context.rulesResponse.size > 1)
        val result = context.rulesResponse.firstOrNull { it.name.contains(filter.searchString) }
            ?: fail("Empty response list")
        assertNotNull(result)
        with(AppRuleStub.get()) {
            assertEquals(grade, result.grade)
            assertEquals(minGrammarPercent, result.minGrammarPercent)
            assertEquals(minLexiconsPercent, result.minLexiconsPercent)
            assertEquals(minListeningPercent, result.minListeningPercent)
            assertEquals(timeWindowDays, result.timeWindowDays)
            assertEquals(priority, result.priority)
        }
    }

    @Test
    fun badId() = runTest {
        val context = AppContext(
            command = AppCommand.SEARCH,
            state = AppState.NONE,
            workMode = AppWorkMode.STUB,
            stubCase = AppStubs.BAD_ID,
            ruleFilterRequest = filter,
        )
        processor.execute(context)
        assertEquals(AppRule(), context.ruleResponse)
        assertEquals("id", context.errors.firstOrNull()?.field)
        assertEquals("validation", context.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val context = AppContext(
            command = AppCommand.SEARCH,
            state = AppState.NONE,
            workMode = AppWorkMode.STUB,
            stubCase = AppStubs.DB_ERROR,
            ruleFilterRequest = filter,
        )
        processor.execute(context)
        assertEquals(AppRule(), context.ruleResponse)
        assertEquals("internal", context.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val context = AppContext(
            command = AppCommand.SEARCH,
            state = AppState.NONE,
            workMode = AppWorkMode.STUB,
            stubCase = AppStubs.BAD_NAME,
            ruleFilterRequest = filter,
        )
        processor.execute(context)
        assertEquals(AppRule(), context.ruleResponse)
        assertEquals("stub", context.errors.firstOrNull()?.field)
    }
}