package com.otus.otuskotlin.skillGrader.project.repo.stubs

import com.otus.otuskotlin.skillGrader.project.common.models.AppGrade
import com.otus.otuskotlin.skillGrader.project.common.repo.DbRuleFilterRequest
import com.otus.otuskotlin.skillGrader.project.common.repo.DbRuleIdRequest
import com.otus.otuskotlin.skillGrader.project.common.repo.DbRuleRequest
import com.otus.otuskotlin.skillGrader.project.common.repo.IDbRuleResponse
import com.otus.otuskotlin.skillGrader.project.common.repo.IDbRulesResponse
import com.otus.otuskotlin.skillGrader.project.common.repo.IRepoRule
import com.otus.otuskotlin.skillGrader.project.stubs.AppRuleStub

class RuleRepoStub() : IRepoRule {
    override suspend fun createRule(request: DbRuleRequest): IDbRuleResponse {
        return IDbRuleResponse.DbRuleSuccessResponse(
            data = AppRuleStub.get(),
        )
    }

    override suspend fun readRule(request: DbRuleIdRequest): IDbRuleResponse {
        return IDbRuleResponse.DbRuleSuccessResponse(
            data = AppRuleStub.get(),
        )
    }

    override suspend fun updateRule(request: DbRuleRequest): IDbRuleResponse {
        return IDbRuleResponse.DbRuleSuccessResponse(
            data = AppRuleStub.get(),
        )
    }

    override suspend fun deleteRule(request: DbRuleIdRequest): IDbRuleResponse {
        return IDbRuleResponse.DbRuleSuccessResponse(
            data = AppRuleStub.get(),
        )
    }

    override suspend fun searchRule(request: DbRuleFilterRequest): IDbRulesResponse {
        return IDbRulesResponse.DbRulesSuccessResponse(
            data = AppRuleStub.prepareSearchList(filter = "", AppGrade.A1),
        )
    }
}
