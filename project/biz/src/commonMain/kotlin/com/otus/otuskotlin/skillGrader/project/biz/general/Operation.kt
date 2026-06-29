package com.otus.otuskotlin.skillGrader.project.biz.general

import ICorChainBuilder
import chain
import com.otus.otuskotlin.skillGrader.project.common.AppContext
import com.otus.otuskotlin.skillGrader.project.common.models.AppCommand
import com.otus.otuskotlin.skillGrader.project.common.models.AppState

fun ICorChainBuilder<AppContext>.operation(
    title: String,
    command: AppCommand,
    block: ICorChainBuilder<AppContext>.() -> Unit
) = chain {
    block()
    this.title = title
    on { this.command == command && state == AppState.RUNNING }
}
