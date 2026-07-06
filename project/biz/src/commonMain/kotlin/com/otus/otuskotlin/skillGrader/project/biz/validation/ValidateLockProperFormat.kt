package com.otus.otuskotlin.skillGrader.project.biz.validation

import ICorChainBuilder
import com.otus.otuskotlin.skillGrader.project.common.AppContext
import com.otus.otuskotlin.skillGrader.project.common.helpers.errorValidation
import com.otus.otuskotlin.skillGrader.project.common.helpers.fail
import com.otus.otuskotlin.skillGrader.project.common.models.AppRuleLock
import worker

fun ICorChainBuilder<AppContext>.validateLockProperFormat(title: String) = worker {
    this.title = title

    // Может быть вынесен в AppRuleId для реализации различных форматов
    val regExp = Regex("^[0-9a-zA-Z-]+$")
    on { ruleValidating.lock != AppRuleLock.NONE && !ruleValidating.lock.asString().matches(regExp) }
    handle {
        val encodedId = ruleValidating.lock.asString()
        fail(
            errorValidation(
                field = "lock",
                violationCode = "badFormat",
                description = "value $encodedId must contain only"
            )
        )
    }
}