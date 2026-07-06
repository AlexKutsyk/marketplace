package com.otus.otuskotlin.skillGrader.project.biz.validation

import ICorChainBuilder
import com.otus.otuskotlin.skillGrader.project.common.AppContext
import com.otus.otuskotlin.skillGrader.project.common.helpers.errorValidation
import com.otus.otuskotlin.skillGrader.project.common.helpers.fail
import worker

fun ICorChainBuilder<AppContext>.validateIdNotEmpty(title: String) = worker {
    this.title = title
    on { ruleValidating.id.asString().isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "id",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}