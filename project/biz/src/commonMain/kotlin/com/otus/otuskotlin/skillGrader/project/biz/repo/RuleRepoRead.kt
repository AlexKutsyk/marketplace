package com.otus.otuskotlin.skillGrader.project.biz.repo

import ICorChainBuilder
import com.otus.otuskotlin.skillGrader.project.common.AppContext
import com.otus.otuskotlin.skillGrader.project.common.helpers.fail
import com.otus.otuskotlin.skillGrader.project.common.models.AppState
import com.otus.otuskotlin.skillGrader.project.common.repo.DbRuleIdRequest
import com.otus.otuskotlin.skillGrader.project.common.repo.IDbRuleResponse
import worker

fun ICorChainBuilder<AppContext>.repoRead(title: String) = worker {
    this.title = title
    description = "Чтение правила из БД"
    on { state == AppState.RUNNING }
    handle {
        val request = DbRuleIdRequest(ruleValidated)
        when (val result = ruleRepo.readRule(request)) {
            is IDbRuleResponse.DbRuleSuccessResponse -> ruleRepoRead = result.data
            is IDbRuleResponse.DbRuleErrorResponse -> fail(result.errors)
            is IDbRuleResponse.DbRuleErrorWithDataResponse -> {
                fail(result.errors)
                ruleRepoRead = result.data
            }
        }
    }
}