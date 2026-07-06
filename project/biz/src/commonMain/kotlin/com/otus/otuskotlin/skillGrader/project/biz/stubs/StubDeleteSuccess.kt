package com.otus.otuskotlin.skillGrader.project.biz.stubs

import ICorChainBuilder
import com.otus.otuskotlin.skillGrader.logging.common.LogLevel
import com.otus.otuskotlin.skillGrader.project.common.AppContext
import com.otus.otuskotlin.skillGrader.project.common.AppCorSettings
import com.otus.otuskotlin.skillGrader.project.common.models.AppState
import com.otus.otuskotlin.skillGrader.project.common.stubs.AppStubs
import com.otus.otuskotlin.skillGrader.project.stubs.AppRuleStub
import worker

fun ICorChainBuilder<AppContext>.stubDeleteSuccess(title: String, corSettings: AppCorSettings) = worker {
    this.title = title
    this.description = """
        Кейс успеха для удаления правила
    """.trimIndent()
    on { stubCase == AppStubs.SUCCESS && state == AppState.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubODeleteSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = AppState.FINISHING
            val stub = AppRuleStub.prepareResult {
                ruleRequest.name.takeIf { it.isNotBlank() }?.also { this.name = it }
            }
            ruleResponse = stub
        }
    }
}