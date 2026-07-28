package com.otus.otuskotlin.skillGrader.project.biz.repo

import ICorChainBuilder
import chain
import com.otus.otuskotlin.skillGrader.project.common.AppContext
import com.otus.otuskotlin.skillGrader.project.common.models.AppState
import com.otus.otuskotlin.skillGrader.project.common.models.AppWorkMode

fun ICorChainBuilder<AppContext>.repo(title: String, block: ICorChainBuilder<AppContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { state == AppState.RUNNING }
}