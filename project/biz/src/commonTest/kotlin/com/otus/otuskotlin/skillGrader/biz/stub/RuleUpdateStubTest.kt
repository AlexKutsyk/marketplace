package com.otus.otuskotlin.skillGrader.biz.stub

import com.otus.otuskotlin.skillGrader.project.biz.RuleProcessor
import com.otus.otuskotlin.skillGrader.project.common.AppContext
import com.otus.otuskotlin.skillGrader.project.common.models.AppCommand
import com.otus.otuskotlin.skillGrader.project.common.models.AppGrade
import com.otus.otuskotlin.skillGrader.project.common.models.AppRule
import com.otus.otuskotlin.skillGrader.project.common.models.AppRuleId
import com.otus.otuskotlin.skillGrader.project.common.models.AppState
import com.otus.otuskotlin.skillGrader.project.common.models.AppWorkMode
import com.otus.otuskotlin.skillGrader.project.common.stubs.AppStubs
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class RuleUpdateStubTest {

    private val processor = RuleProcessor()
    val id = AppRuleId("777")
    val name = "Beginner A2"
    val grade = AppGrade.A2
    val minGrammarPercent = 20
    val minLexiconsPercent = 20
    val minListeningPercent = 20
    val timeWindowDays = 0
    val priority = 2

    @Test
    fun update() = runTest {

        val context = AppContext(
            command = AppCommand.UPDATE,
            state = AppState.NONE,
            workMode = AppWorkMode.STUB,
            stubCase = AppStubs.SUCCESS,
            ruleRequest = AppRule(
                id = id,
                name = name,
                grade = grade,
                minGrammarPercent = minGrammarPercent,
                minLexiconsPercent = minLexiconsPercent,
                minListeningPercent = minListeningPercent,
                timeWindowDays = timeWindowDays,
                priority = priority
            ),
        )
        processor.execute(context)
        assertEquals(id, context.ruleResponse.id)
        assertEquals(name, context.ruleResponse.name)
        assertEquals(grade, context.ruleResponse.grade)
        assertEquals(minGrammarPercent, context.ruleResponse.minGrammarPercent)
        assertEquals(minLexiconsPercent, context.ruleResponse.minLexiconsPercent)
        assertEquals(minListeningPercent, context.ruleResponse.minListeningPercent)
        assertEquals(timeWindowDays, context.ruleResponse.timeWindowDays)
        assertEquals(priority, context.ruleResponse.priority)
    }

    @Test
    fun badId() = runTest {
        val context = AppContext(
            command = AppCommand.UPDATE,
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
    fun badName() = runTest {
        val context = AppContext(
            command = AppCommand.UPDATE,
            state = AppState.NONE,
            workMode = AppWorkMode.STUB,
            stubCase = AppStubs.BAD_NAME,
            ruleRequest = AppRule(
                id = id,
                name = "",
                grade = grade,
                minGrammarPercent = minGrammarPercent,
                minLexiconsPercent = minLexiconsPercent,
                minListeningPercent = minListeningPercent,
                timeWindowDays = timeWindowDays,
                priority = priority
            ),
        )
        processor.execute(context)
        assertEquals(AppRule(), context.ruleResponse)
        assertEquals("name", context.errors.firstOrNull()?.field)
        assertEquals("validation", context.errors.firstOrNull()?.group)
    }

    @Test
    fun badMinGrammarPercent() = runTest {
        val context = AppContext(
            command = AppCommand.UPDATE,
            state = AppState.NONE,
            workMode = AppWorkMode.STUB,
            stubCase = AppStubs.BAD_MIN_GRAMMAR_PERCENT,
            ruleRequest = AppRule(
                id = id,
                name = name,
                grade = grade,
                minGrammarPercent = minGrammarPercent,
                minLexiconsPercent = minLexiconsPercent,
                minListeningPercent = minListeningPercent,
                timeWindowDays = timeWindowDays,
                priority = priority
            ),
        )
        processor.execute(context)
        assertEquals(AppRule(), context.ruleResponse)
        assertEquals("minGrammarPercent", context.errors.firstOrNull()?.field)
        assertEquals("validation", context.errors.firstOrNull()?.group)
    }

    @Test
    fun badMinLexiconsPercent() = runTest {
        val context = AppContext(
            command = AppCommand.UPDATE,
            state = AppState.NONE,
            workMode = AppWorkMode.STUB,
            stubCase = AppStubs.BAD_MIN_LEXICONS_PERCENT,
            ruleRequest = AppRule(
                id = id,
                name = name,
                grade = grade,
                minGrammarPercent = minGrammarPercent,
                minLexiconsPercent = minLexiconsPercent,
                minListeningPercent = minListeningPercent,
                timeWindowDays = timeWindowDays,
                priority = priority
            ),
        )
        processor.execute(context)
        assertEquals(AppRule(), context.ruleResponse)
        assertEquals("minLexiconsPercent", context.errors.firstOrNull()?.field)
        assertEquals("validation", context.errors.firstOrNull()?.group)
    }

    @Test
    fun badMinListeningPercent() = runTest {
        val context = AppContext(
            command = AppCommand.UPDATE,
            state = AppState.NONE,
            workMode = AppWorkMode.STUB,
            stubCase = AppStubs.BAD_MIN_LISTENING_PERCENT,
            ruleRequest = AppRule(
                id = id,
                name = name,
                grade = grade,
                minGrammarPercent = minGrammarPercent,
                minLexiconsPercent = minLexiconsPercent,
                minListeningPercent = minListeningPercent,
                timeWindowDays = timeWindowDays,
                priority = priority
            ),
        )
        processor.execute(context)
        assertEquals(AppRule(), context.ruleResponse)
        assertEquals("minListeningPercent", context.errors.firstOrNull()?.field)
        assertEquals("validation", context.errors.firstOrNull()?.group)
    }

    @Test
    fun badTimeWindowsDays() = runTest {
        val context = AppContext(
            command = AppCommand.UPDATE,
            state = AppState.NONE,
            workMode = AppWorkMode.STUB,
            stubCase = AppStubs.BAD_TIME_WINDOW_DAYS,
            ruleRequest = AppRule(
                id = id,
                name = name,
                grade = grade,
                minGrammarPercent = minGrammarPercent,
                minLexiconsPercent = minLexiconsPercent,
                minListeningPercent = minListeningPercent,
                timeWindowDays = timeWindowDays,
                priority = priority
            ),
        )
        processor.execute(context)
        assertEquals(AppRule(), context.ruleResponse)
        assertEquals("timeWindowDays", context.errors.firstOrNull()?.field)
        assertEquals("validation", context.errors.firstOrNull()?.group)
    }

    @Test
    fun badPriority() = runTest {
        val context = AppContext(
            command = AppCommand.UPDATE,
            state = AppState.NONE,
            workMode = AppWorkMode.STUB,
            stubCase = AppStubs.BAD_PRIORITY,
            ruleRequest = AppRule(
                id = id,
                name = name,
                grade = grade,
                minGrammarPercent = minGrammarPercent,
                minLexiconsPercent = minLexiconsPercent,
                minListeningPercent = minListeningPercent,
                timeWindowDays = timeWindowDays,
                priority = priority
            ),
        )
        processor.execute(context)
        assertEquals(AppRule(), context.ruleResponse)
        assertEquals("priority", context.errors.firstOrNull()?.field)
        assertEquals("validation", context.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val context = AppContext(
            command = AppCommand.UPDATE,
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
            command = AppCommand.UPDATE,
            state = AppState.NONE,
            workMode = AppWorkMode.STUB,
            stubCase = AppStubs.BAD_SEARCH_STRING,
            ruleRequest = AppRule(
                id = id,
                name = name,
                grade = grade,
                minGrammarPercent = minGrammarPercent,
                minLexiconsPercent = minLexiconsPercent,
                minListeningPercent = minListeningPercent,
                timeWindowDays = timeWindowDays,
                priority = priority
            ),
        )
        processor.execute(context)
        assertEquals(AppRule(), context.ruleResponse)
        assertEquals("stub", context.errors.firstOrNull()?.field)
        assertEquals("validation", context.errors.firstOrNull()?.group)
    }
}