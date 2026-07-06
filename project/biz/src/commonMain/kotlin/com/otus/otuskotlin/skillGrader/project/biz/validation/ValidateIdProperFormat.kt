package com.otus.otuskotlin.skillGrader.project.biz.validation

import ICorChainBuilder
import com.otus.otuskotlin.skillGrader.project.common.AppContext
import com.otus.otuskotlin.skillGrader.project.common.helpers.errorValidation
import com.otus.otuskotlin.skillGrader.project.common.helpers.fail
import com.otus.otuskotlin.skillGrader.project.common.models.AppRuleId
import worker

fun ICorChainBuilder<AppContext>.validateIdProperFormat(title: String) = worker {
    this.title = title

    // Может быть вынесен в AppRuleId для реализации различных форматов
    val regExp = Regex("^[0-9a-zA-Z#:-]+$")
    on { ruleValidating.id != AppRuleId.NONE && !ruleValidating.id.asString().matches(regExp) }
    handle {
        val encodedId = ruleValidating.id.asString()
            .replace("<", "&lt;")
            .replace(">", "&gt;")
        fail(
            errorValidation(
                field = "id",
                violationCode = "badFormat",
                description = "value $encodedId must contain only letters and numbers"
            )
        )
    }
}