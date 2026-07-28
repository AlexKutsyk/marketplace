package com.otus.otuskotlin.skillGrader.project.biz.repo

import ICorChainBuilder
import com.otus.otuskotlin.skillGrader.project.common.AppContext
import com.otus.otuskotlin.skillGrader.project.common.helpers.fail
import com.otus.otuskotlin.skillGrader.project.common.models.AppState
import com.otus.otuskotlin.skillGrader.project.common.repo.errorRepoConcurrency
import worker

fun ICorChainBuilder<AppContext>.checkLock(title: String) = worker {
    this.title = title
    description = """
        Проверка оптимистичной блокировки. Если не равна сохраненной в БД, значит данные запроса устарели 
        и необходимо их обновить вручную
    """.trimIndent()
    on { state == AppState.RUNNING && ruleValidated.lock != ruleRepoRead.lock }
    handle {
        fail(errorRepoConcurrency(ruleRepoRead, ruleValidated.lock).errors)
    }
}