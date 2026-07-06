package com.otus.otuskotlin.skillGrader.project.biz.stubs

import ICorChainBuilder
import com.otus.otuskotlin.skillGrader.logging.common.LogLevel
import com.otus.otuskotlin.skillGrader.project.common.AppContext
import com.otus.otuskotlin.skillGrader.project.common.AppCorSettings
import com.otus.otuskotlin.skillGrader.project.common.models.AppGrade
import com.otus.otuskotlin.skillGrader.project.common.models.AppState
import com.otus.otuskotlin.skillGrader.project.common.stubs.AppStubs
import com.otus.otuskotlin.skillGrader.project.stubs.AppRuleStub
import worker

fun ICorChainBuilder<AppContext>.stubCreateSuccess(title: String, corSettings: AppCorSettings) = worker {
    this.title = title
    this.description = """
        Кейс успеха для создания правила
    """.trimIndent()
    on { stubCase == AppStubs.SUCCESS && state == AppState.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubCreateSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = AppState.FINISHING
            val stub = AppRuleStub.prepareResult {
                ruleRequest.name.takeIf { it.isNotBlank() }?.also { this.name = it }
                ruleRequest.grade.takeIf { it != AppGrade.NONE }?.also { this.grade = it }
                ruleRequest.minGrammarPercent.takeIf { it in 0..100 }?.also { this.minGrammarPercent = it }
                ruleRequest.minLexiconsPercent.takeIf { it in 0..100 }?.also { this.minLexiconsPercent = it }
                ruleRequest.minListeningPercent.takeIf { it in 0..100 }?.also { this.minListeningPercent = it }
                ruleRequest.timeWindowDays.takeIf { it >= 0 }?.also { this.timeWindowDays = it }
                ruleRequest.priority.takeIf { it >= 0 }?.also { this.priority = it }
            }
            ruleResponse = stub
        }
    }
}