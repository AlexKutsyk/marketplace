package com.otus.otuskotlin.skillGrader.project.biz.validation

import ICorChainBuilder
import chain
import com.otus.otuskotlin.skillGrader.project.common.AppContext
import com.otus.otuskotlin.skillGrader.project.common.models.AppState

fun ICorChainBuilder<AppContext>.validation(block: ICorChainBuilder<AppContext>.() -> Unit) = chain {
    block()
    title = "Валидация"

    on { state == AppState.RUNNING }
}