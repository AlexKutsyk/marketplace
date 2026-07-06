package com.otus.otuskotlin.skillGrader.biz.stub

import com.otus.otuskotlin.skillGrader.project.biz.RuleProcessor
import com.otus.otuskotlin.skillGrader.project.common.AppContext
import com.otus.otuskotlin.skillGrader.project.common.models.AppCommand
import com.otus.otuskotlin.skillGrader.project.common.models.AppRule
import com.otus.otuskotlin.skillGrader.project.common.models.AppRuleId
import com.otus.otuskotlin.skillGrader.project.common.models.AppState
import com.otus.otuskotlin.skillGrader.project.common.models.AppWorkMode
import com.otus.otuskotlin.skillGrader.project.common.stubs.AppStubs
import com.otus.otuskotlin.skillGrader.project.stubs.AppRuleStub
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class RuleReadStubTest {

    private val processor = RuleProcessor()
    val id = AppRuleId("007")

    @Test
    fun read() = runTest {

        val context = AppContext(
            command = AppCommand.READ,
            state = AppState.NONE,
            workMode = AppWorkMode.STUB,
            stubCase = AppStubs.SUCCESS,
            ruleRequest = AppRule(id = id),
        )
        processor.execute(context)
        with (AppRuleStub.get()) {
            assertEquals(id, context.ruleResponse.id)
            assertEquals(name, context.ruleResponse.name)
            assertEquals(grade, context.ruleResponse.grade)
            assertEquals(minGrammarPercent, context.ruleResponse.minGrammarPercent)
            assertEquals(minLexiconsPercent, context.ruleResponse.minLexiconsPercent)
            assertEquals(minListeningPercent, context.ruleResponse.minListeningPercent)
            assertEquals(timeWindowDays, context.ruleResponse.timeWindowDays)
            assertEquals(priority, context.ruleResponse.priority)
        }
    }

    @Test
    fun badId() = runTest {
        val context = AppContext(
            command = AppCommand.READ,
            state = AppState.NONE,
            workMode = AppWorkMode.STUB,
            stubCase = AppStubs.BAD_ID,
            ruleRequest = AppRule(),
        )
        processor.execute(context)
        assertEquals(AppRule(), context.ruleResponse)
        assertEquals("id", context.errors.firstOrNull()?.field)
        assertEquals("validation", context.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val context = AppContext(
            command = AppCommand.READ,
            state = AppState.NONE,
            workMode = AppWorkMode.STUB,
            stubCase = AppStubs.DB_ERROR,
            ruleRequest = AppRule(id = id),
        )
        processor.execute(context)
        assertEquals(AppRule(), context.ruleResponse)
        assertEquals("internal", context.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val context = AppContext(
            command = AppCommand.READ,
            state = AppState.NONE,
            workMode = AppWorkMode.STUB,
            stubCase = AppStubs.BAD_NAME,
            ruleRequest = AppRule(id = id),
        )
        processor.execute(context)
        assertEquals(AppRule(), context.ruleResponse)
        assertEquals("stub", context.errors.firstOrNull()?.field)
    }
}