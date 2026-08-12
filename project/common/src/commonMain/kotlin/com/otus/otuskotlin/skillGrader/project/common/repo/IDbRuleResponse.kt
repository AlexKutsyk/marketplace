package com.otus.otuskotlin.skillGrader.project.common.repo

import com.otus.otuskotlin.skillGrader.project.common.models.AppError
import com.otus.otuskotlin.skillGrader.project.common.models.AppRule

sealed interface IDbRuleResponse: IDbResponse<AppRule> {

    data class DbRuleSuccessResponse(val data: AppRule): IDbRuleResponse

    data class DbRuleErrorResponse(val errors: List<AppError> = emptyList()): IDbRuleResponse {
        constructor(error: AppError): this(listOf(error))
    }

    data class DbRuleErrorWithDataResponse(
        val data: AppRule,
        val errors: List<AppError> = emptyList()
    ): IDbRuleResponse {
        constructor(rule: AppRule, error: AppError): this(rule, listOf(error))
    }
}