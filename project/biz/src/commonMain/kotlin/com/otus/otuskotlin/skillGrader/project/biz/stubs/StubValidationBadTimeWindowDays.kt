package com.otus.otuskotlin.skillGrader.project.biz.stubs

import ICorChainBuilder
import com.otus.otuskotlin.skillGrader.project.common.AppContext
import com.otus.otuskotlin.skillGrader.project.common.helpers.fail
import com.otus.otuskotlin.skillGrader.project.common.models.AppError
import com.otus.otuskotlin.skillGrader.project.common.models.AppState
import com.otus.otuskotlin.skillGrader.project.common.stubs.AppStubs
import worker

fun ICorChainBuilder<AppContext>.stubValidationBadTimeWindowDays(title: String) = worker {
    this.title = title
    this.description = """
        Кейс ошибки валидации параметра правила timeWindowDays
    """.trimIndent()
    on { stubCase == AppStubs.BAD_TIME_WINDOW_DAYS && state == AppState.RUNNING }
    handle {
        fail(
            AppError(
                group = "validation",
                code = "validation-timeWindowDays",
                field = "timeWindowDays",
                message = "Wrong timeWindowDays field"
            )
        )
    }
}