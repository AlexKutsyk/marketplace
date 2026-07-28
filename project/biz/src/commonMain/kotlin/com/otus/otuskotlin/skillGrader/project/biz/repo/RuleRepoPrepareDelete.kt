package com.otus.otuskotlin.skillGrader.project.biz.repo

import ICorChainBuilder
import com.otus.otuskotlin.skillGrader.project.common.AppContext
import com.otus.otuskotlin.skillGrader.project.common.models.AppState
import worker

fun ICorChainBuilder<AppContext>.repoPrepareDelete(title: String) = worker {
    this.title = title
    description = """
        Готовим данные к удалению из БД
    """.trimIndent()
    on { state == AppState.RUNNING }
    handle {
        ruleRepoPrepare = ruleValidated.deepCopy()
    }
}
