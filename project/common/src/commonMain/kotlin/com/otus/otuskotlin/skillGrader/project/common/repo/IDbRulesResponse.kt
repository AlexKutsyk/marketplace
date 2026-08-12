package com.otus.otuskotlin.skillGrader.project.common.repo

import com.otus.otuskotlin.skillGrader.project.common.models.AppError
import com.otus.otuskotlin.skillGrader.project.common.models.AppRule

sealed interface IDbRulesResponse: IDbResponse<List<AppRule>> {

    data class DbRulesSuccessResponse(val data: List<AppRule>): IDbRulesResponse

//    @Suppress("unused")
    data class DbRulesErrorResponse(val errors: List<AppError> = emptyList()): IDbRulesResponse {
        constructor(error: AppError): this(listOf(error))
    }
}