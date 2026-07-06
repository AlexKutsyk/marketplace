package com.otus.otuskotlin.skillGrader.project.biz.validation

import ICorChainBuilder
import com.otus.otuskotlin.skillGrader.project.common.AppContext
import com.otus.otuskotlin.skillGrader.project.common.helpers.errorValidation
import com.otus.otuskotlin.skillGrader.project.common.helpers.fail
import worker

// смотрим пример COR DSL валидации
fun ICorChainBuilder<AppContext>.validateNameNotEmpty(title: String) = worker {
    this.title = title
    on { ruleValidating.name.isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "name",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}