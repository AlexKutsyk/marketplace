package com.otus.otuskotlin.skillGrader.project.biz.repo

import ICorChainBuilder
import com.otus.otuskotlin.skillGrader.project.common.AppContext
import com.otus.otuskotlin.skillGrader.project.common.models.AppState
import worker

fun ICorChainBuilder<AppContext>.repoDoneRead(title: String) = worker {
    this.title = title
    description = "Подготовка ответа для Read"
    on { state == AppState.RUNNING }
    handle { ruleRepoDone = ruleRepoRead }
}