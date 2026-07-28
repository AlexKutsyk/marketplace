package com.otus.otuskotlin.skillGrader.project.biz.repo

import ICorChainBuilder
import com.otus.otuskotlin.skillGrader.project.common.AppContext
import com.otus.otuskotlin.skillGrader.project.common.models.AppState
import worker


fun ICorChainBuilder<AppContext>.repoPrepareCreate(title: String) = worker {
    this.title = title
    description = "Подготовка объекта к сохранению в базе данных"
    on { state == AppState.RUNNING }
    handle {
        ruleRepoPrepare = ruleValidated.deepCopy()
    }
}
