package com.otus.otuskotlin.skillGrader.project.biz.repo

import ICorChainBuilder
import com.otus.otuskotlin.skillGrader.project.common.AppContext
import com.otus.otuskotlin.skillGrader.project.common.helpers.fail
import com.otus.otuskotlin.skillGrader.project.common.models.AppState
import com.otus.otuskotlin.skillGrader.project.common.repo.DbRuleFilterRequest
import com.otus.otuskotlin.skillGrader.project.common.repo.IDbRulesResponse
import worker

fun ICorChainBuilder<AppContext>.repoSearch(title: String) = worker {
    this.title = title
    description = "Поиск правила в БД по фильтру"
    on { state == AppState.RUNNING }
    handle {
        val request = DbRuleFilterRequest(
            nameFilter = ruleFilterValidated.searchString,
            grade = ruleFilterValidated.grade
        )
        when(val result = ruleRepo.searchRule(request)) {
            is IDbRulesResponse.DbRulesSuccessResponse -> rulesRepoDone = result.data.toMutableList()
            is IDbRulesResponse.DbRulesErrorResponse -> fail(result.errors)
        }
    }
}