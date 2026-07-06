package com.otus.otuskotlin.skillGrader.project.biz.validation

import ICorChainBuilder
import com.otus.otuskotlin.skillGrader.project.common.AppContext
import com.otus.otuskotlin.skillGrader.project.common.helpers.errorValidation
import com.otus.otuskotlin.skillGrader.project.common.helpers.fail
import worker

// пример обработки ошибки в рамках бизнес-цепочки
fun ICorChainBuilder<AppContext>.validateRangeMinLexiconsPercent(title: String) = worker {
    this.title = title

    on { ruleValidating.minLexiconsPercent !in 0 .. 100 }
    handle {
        fail(
            errorValidation(
                field = "minLexiconsPercent",
                violationCode = "outOfRange",
                description = "must be between 0 and 100"
            )
        )
    }
}