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
import com.otus.otuskotlin.skillGrader.project.stubs.AppRuleStub
import kotlinx.coroutines.test.runTest
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val stub = AppRuleStub.get()

fun validationNameCorrect(command: AppCommand, processor: RuleProcessor) = runTest {
    val context = AppContext(
        command = command,
        state = AppState.NONE,
        workMode = AppWorkMode.TEST,
        ruleRequest = AppRule(
            id = stub.id,
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
    assertEquals(context.ruleRequest.name, context.ruleValidated.name)
}

fun validationNameTrim(command: AppCommand, processor: RuleProcessor) = runTest {
    val context = AppContext(
        command = command,
        state = AppState.NONE,
        workMode = AppWorkMode.TEST,
        ruleRequest = AppRule(
            id = stub.id,
            name = " \n\t Elementary A1 \t\n ",
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
    assertEquals(context.ruleRequest.name.trim(), context.ruleValidated.name)
}

fun validationNameEmpty(command: AppCommand, processor: RuleProcessor) = runTest {
    val context = AppContext(
        command = command,
        state = AppState.NONE,
        workMode = AppWorkMode.TEST,
        ruleRequest = AppRule(
            id = stub.id,
            name = "",
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
    assertEquals("name", error?.field)
    assertContains(error?.message ?: "", "name")
}

fun validationNameSymbols(command: AppCommand, processor: RuleProcessor) = runTest {
    val context = AppContext(
        command = command,
        state = AppState.NONE,
        workMode = AppWorkMode.TEST,
        ruleRequest = AppRule(
            id = AppRuleId("123"),
            name = "!@#$%^&*(),.{}",
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
    assertEquals("name", error?.field)
    assertContains(error?.message ?: "", "name")
}