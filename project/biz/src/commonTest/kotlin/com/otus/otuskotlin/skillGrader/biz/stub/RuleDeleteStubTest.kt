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

class RuleDeleteStubTest {

    private val processor = RuleProcessor()
    val id = AppRuleId("007")

    @Test
    fun delete() = runTest {

        val context = AppContext(
            command = AppCommand.DELETE,
            state = AppState.NONE,
            workMode = AppWorkMode.STUB,
            stubCase = AppStubs.SUCCESS,
            ruleRequest = AppRule(id = id),
        )
        processor.execute(context)

        val stub = AppRuleStub.get()
        assertEquals(stub.id, context.ruleResponse.id)
        assertEquals(stub.name, context.ruleResponse.name)
        assertEquals(stub.grade, context.ruleResponse.grade)
        assertEquals(stub.minGrammarPercent, context.ruleResponse.minGrammarPercent)
        assertEquals(stub.minLexiconsPercent, context.ruleResponse.minLexiconsPercent)
        assertEquals(stub.minListeningPercent, context.ruleResponse.minListeningPercent)
        assertEquals(stub.timeWindowDays, context.ruleResponse.timeWindowDays)
        assertEquals(stub.priority, context.ruleResponse.priority)
    }

    @Test
    fun badId() = runTest {
        val context = AppContext(
            command = AppCommand.DELETE,
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
            command = AppCommand.DELETE,
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
            command = AppCommand.DELETE,
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