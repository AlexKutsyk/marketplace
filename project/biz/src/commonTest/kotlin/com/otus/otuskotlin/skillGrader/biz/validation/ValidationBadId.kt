package com.otus.otuskotlin.skillGrader.biz.validation

import com.otus.otuskotlin.skillGrader.project.biz.RuleProcessor
import com.otus.otuskotlin.skillGrader.project.common.AppContext
import com.otus.otuskotlin.skillGrader.project.common.models.AppCommand
import com.otus.otuskotlin.skillGrader.project.common.models.AppGrade
import com.otus.otuskotlin.skillGrader.project.common.models.AppRule
import com.otus.otuskotlin.skillGrader.project.common.models.AppRuleId
import com.otus.otuskotlin.skillGrader.project.common.models.AppRuleLock
import com.otus.otuskotlin.skillGrader.project.common.models.AppState
import com.otus.otuskotlin.skillGrader.project.common.models.AppWorkMode
import kotlinx.coroutines.test.runTest
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

fun validationIdCorrect(command: AppCommand, processor: RuleProcessor) = runTest {
    val context = AppContext(
        command = command,
        state = AppState.NONE,
        workMode = AppWorkMode.TEST,
        ruleRequest = AppRule(
            id = AppRuleId("123-234-abc-ABC"),
            name = "Elementary A1",
            grade = AppGrade.A1,
            minGrammarPercent = 15,
            minLexiconsPercent = 15,
            minListeningPercent = 15,
            timeWindowDays = 14,
            priority = 1,
            lock = AppRuleLock("123-234-abc-ABC"),
        ),
    )
    processor.execute(context)
    assertEquals(0, context.errors.size)
    assertNotEquals(AppState.FAILING, context.state)
}

fun validationIdTrim(command: AppCommand, processor: RuleProcessor) = runTest {
    val context = AppContext(
        command = command,
        state = AppState.NONE,
        workMode = AppWorkMode.TEST,
        ruleRequest = AppRule(
            id = AppRuleId(" \n\t 123-234-abc-ABC \n\t "),
            name = "Elementary A1",
            grade = AppGrade.A1,
            minGrammarPercent = 15,
            minLexiconsPercent = 15,
            minListeningPercent = 15,
            timeWindowDays = 14,
            priority = 1,
            lock = AppRuleLock("123-234-abc-ABC"),
        ),
    )
    processor.execute(context)
    assertEquals(0, context.errors.size)
    assertNotEquals(AppState.FAILING, context.state)
}

fun validationIdEmpty(command: AppCommand, processor: RuleProcessor) = runTest {
    val context = AppContext(
        command = command,
        state = AppState.NONE,
        workMode = AppWorkMode.TEST,
        ruleRequest = AppRule(
            id = AppRuleId(""),
            name = "Elementary A1",
            grade = AppGrade.A1,
            minGrammarPercent = 15,
            minLexiconsPercent = 15,
            minListeningPercent = 15,
            timeWindowDays = 14,
            priority = 1,
            lock = AppRuleLock("123-234-abc-ABC"),
        ),
    )
    processor.execute(context)
    assertEquals(1, context.errors.size)
    assertEquals(AppState.FAILING, context.state)
    val error = context.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
}

fun validationIdFormat(command: AppCommand, processor: RuleProcessor) = runTest {
    val context = AppContext(
        command = command,
        state = AppState.NONE,
        workMode = AppWorkMode.TEST,
        ruleRequest = AppRule(
            id = AppRuleId("!@#\$%^&*(),.{}"),
            name = "Elementary A1",
            grade = AppGrade.A1,
            minGrammarPercent = 15,
            minLexiconsPercent = 15,
            minListeningPercent = 15,
            timeWindowDays = 14,
            priority = 1,
            lock = AppRuleLock("123-234-abc-ABC"),
        ),
    )
    processor.execute(context)
    assertEquals(1, context.errors.size)
    assertEquals(AppState.FAILING, context.state)
    val error = context.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
}