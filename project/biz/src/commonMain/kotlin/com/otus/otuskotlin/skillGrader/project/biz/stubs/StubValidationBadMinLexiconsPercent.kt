package com.otus.otuskotlin.skillGrader.project.biz.stubs

import ICorChainBuilder
import com.otus.otuskotlin.skillGrader.project.common.AppContext
import com.otus.otuskotlin.skillGrader.project.common.helpers.fail
import com.otus.otuskotlin.skillGrader.project.common.models.AppError
import com.otus.otuskotlin.skillGrader.project.common.models.AppState
import com.otus.otuskotlin.skillGrader.project.common.stubs.AppStubs
import worker

fun ICorChainBuilder<AppContext>.stubValidationBadMinLexiconsPercent(title: String) = worker {
    this.title = title
    this.description = """
        Кейс ошибки валидации параметра правила minLexiconsPercent
    """.trimIndent()
    on { stubCase == AppStubs.BAD_MIN_LEXICONS_PERCENT && state == AppState.RUNNING }
    handle {
        fail(
            AppError(
                group = "validation",
                code = "validation-minLexiconsPercent",
                field = "minLexiconsPercent",
                message = "Wrong minLexiconsPercent field"
            )
        )
    }
}