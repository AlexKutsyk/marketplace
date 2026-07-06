package com.otus.otuskotlin.skillGrader.project.biz.stubs

import ICorChainBuilder
import com.otus.otuskotlin.skillGrader.project.common.AppContext
import com.otus.otuskotlin.skillGrader.project.common.helpers.fail
import com.otus.otuskotlin.skillGrader.project.common.models.AppError
import com.otus.otuskotlin.skillGrader.project.common.models.AppState
import com.otus.otuskotlin.skillGrader.project.common.stubs.AppStubs
import worker

fun ICorChainBuilder<AppContext>.stubValidationBadPriority(title: String) = worker {
    this.title = title
    this.description = """
        Кейс ошибки валидации параметра правила priority
    """.trimIndent()
    on { stubCase == AppStubs.BAD_PRIORITY && state == AppState.RUNNING }
    handle {
        fail(
            AppError(
                group = "validation",
                code = "validation-priority",
                field = "priority",
                message = "Wrong priority field"
            )
        )
    }
}