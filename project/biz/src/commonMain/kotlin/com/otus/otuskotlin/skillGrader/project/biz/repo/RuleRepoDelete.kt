package com.otus.otuskotlin.skillGrader.project.biz.repo

import ICorChainBuilder
import com.otus.otuskotlin.skillGrader.project.common.AppContext
import com.otus.otuskotlin.skillGrader.project.common.helpers.fail
import com.otus.otuskotlin.skillGrader.project.common.models.AppState
import com.otus.otuskotlin.skillGrader.project.common.repo.DbRuleIdRequest
import com.otus.otuskotlin.skillGrader.project.common.repo.IDbRuleResponse
import worker

fun ICorChainBuilder<AppContext>.repoDelete(title: String) = worker {
    this.title = title
    description = "Удаление объявления из БД по ID"
    on { state == AppState.RUNNING }
    handle {
        val request = DbRuleIdRequest(ruleRepoPrepare)
        when (val result = ruleRepo.deleteRule(request)) {
            is IDbRuleResponse.DbRuleSuccessResponse -> ruleRepoDone = result.data
            is IDbRuleResponse.DbRuleErrorResponse -> fail(result.errors)

            is IDbRuleResponse.DbRuleErrorWithDataResponse -> {
                fail(result.errors)
                ruleRepoDone = result.data
            }
        }
    }
}