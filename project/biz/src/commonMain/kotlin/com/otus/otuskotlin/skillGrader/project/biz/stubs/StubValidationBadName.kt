package com.otus.otuskotlin.skillGrader.project.biz.stubs

import ICorChainBuilder
import com.otus.otuskotlin.skillGrader.project.common.AppContext
import com.otus.otuskotlin.skillGrader.project.common.helpers.fail
import com.otus.otuskotlin.skillGrader.project.common.models.AppError
import com.otus.otuskotlin.skillGrader.project.common.models.AppState
import com.otus.otuskotlin.skillGrader.project.common.stubs.AppStubs
import worker

fun ICorChainBuilder<AppContext>.stubValidationBadName(title: String) = worker {
    this.title = title
    this.description = """
        Кейс ошибки валидации названия
    """.trimIndent()

    on { stubCase == AppStubs.BAD_NAME && state == AppState.RUNNING }
    handle {
        fail(
            AppError(
                group = "validation",
                code = "validation-name",
                field = "name",
                message = "Wrong name field"
            )
        )
    }
}