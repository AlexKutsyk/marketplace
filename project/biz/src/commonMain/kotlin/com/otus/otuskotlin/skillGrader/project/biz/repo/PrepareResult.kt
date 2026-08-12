package com.otus.otuskotlin.skillGrader.project.biz.repo

import ICorChainBuilder
import com.otus.otuskotlin.skillGrader.project.common.AppContext
import com.otus.otuskotlin.skillGrader.project.common.models.AppState
import com.otus.otuskotlin.skillGrader.project.common.models.AppWorkMode
import worker

fun ICorChainBuilder<AppContext>.prepareResult(title: String) = worker {
    this.title = title
    description = "Подготовка данных для ответа клиенту на запрос"
    on { workMode != AppWorkMode.STUB }
    handle {
        ruleResponse = ruleRepoDone
        rulesResponse = rulesRepoDone
        state = when (val st = state) {
            AppState.RUNNING -> AppState.FINISHING
            else -> st
        }
    }
}