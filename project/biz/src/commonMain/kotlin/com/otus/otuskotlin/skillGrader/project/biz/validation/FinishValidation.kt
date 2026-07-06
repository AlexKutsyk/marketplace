package com.otus.otuskotlin.skillGrader.project.biz.validation

import ICorChainBuilder
import com.otus.otuskotlin.skillGrader.project.common.AppContext
import com.otus.otuskotlin.skillGrader.project.common.models.AppState
import worker

fun ICorChainBuilder<AppContext>.finishRuleValidation(title: String) = worker {
    this.title = title
    on { state == AppState.RUNNING }
    handle {
        ruleValidated = ruleValidating
    }
}

fun ICorChainBuilder<AppContext>.finishRuleFilterValidation(title: String) = worker {
    this.title = title
    on { state == AppState.RUNNING }
    handle {
        ruleFilterValidated = ruleFilterValidating
    }
}