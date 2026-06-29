package com.otus.otuskotlin.skillGrader.project.biz.general

import ICorChainBuilder
import chain
import com.otus.otuskotlin.skillGrader.project.common.AppContext
import com.otus.otuskotlin.skillGrader.project.common.models.AppState
import com.otus.otuskotlin.skillGrader.project.common.models.AppWorkMode

fun ICorChainBuilder<AppContext>.stubs(title: String, block: ICorChainBuilder<AppContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { workMode == AppWorkMode.STUB && state == AppState.RUNNING }
}