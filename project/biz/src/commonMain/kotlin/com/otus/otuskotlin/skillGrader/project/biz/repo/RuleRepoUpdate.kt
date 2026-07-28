package com.otus.otuskotlin.skillGrader.project.biz.repo

import ICorChainBuilder
import com.otus.otuskotlin.skillGrader.project.common.AppContext
import com.otus.otuskotlin.skillGrader.project.common.helpers.fail
import com.otus.otuskotlin.skillGrader.project.common.models.AppState
import com.otus.otuskotlin.skillGrader.project.common.repo.DbRuleRequest
import com.otus.otuskotlin.skillGrader.project.common.repo.IDbRuleResponse
import worker

fun ICorChainBuilder<AppContext>.repoUpdate(title: String) = worker {
    this.title = title
    on { state == AppState.RUNNING }
    handle {
        val request = DbRuleRequest(ruleRepoPrepare)
        when (val result = ruleRepo.updateRule(request)) {
            is IDbRuleResponse.DbRuleSuccessResponse -> ruleRepoDone = result.data
            is IDbRuleResponse.DbRuleErrorResponse -> fail(result.errors)
            is IDbRuleResponse.DbRuleErrorWithDataResponse -> {
                fail(result.errors)
                ruleRepoDone = result.data
            }
        }
    }
}