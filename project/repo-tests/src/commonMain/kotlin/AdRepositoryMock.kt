package com.otus.otuskotlin.skillGrader.project.repo.tests

import com.otus.otuskotlin.skillGrader.project.common.models.AppRule
import com.otus.otuskotlin.skillGrader.project.common.repo.DbRuleFilterRequest
import com.otus.otuskotlin.skillGrader.project.common.repo.DbRuleIdRequest
import com.otus.otuskotlin.skillGrader.project.common.repo.DbRuleRequest
import com.otus.otuskotlin.skillGrader.project.common.repo.IDbRuleResponse
import com.otus.otuskotlin.skillGrader.project.common.repo.IDbRulesResponse
import com.otus.otuskotlin.skillGrader.project.common.repo.IRepoRule

class RuleRepositoryMock(
    private val invokeCreateRule: (DbRuleRequest) -> IDbRuleResponse = { DEFAULT_RULE_SUCCESS_EMPTY_MOCK },
    private val invokeReadRule: (DbRuleIdRequest) -> IDbRuleResponse = { DEFAULT_RULE_SUCCESS_EMPTY_MOCK },
    private val invokeUpdateRule: (DbRuleRequest) -> IDbRuleResponse = { DEFAULT_RULE_SUCCESS_EMPTY_MOCK },
    private val invokeDeleteRule: (DbRuleIdRequest) -> IDbRuleResponse = { DEFAULT_RULE_SUCCESS_EMPTY_MOCK },
    private val invokeSearchRule: (DbRuleFilterRequest) -> IDbRulesResponse = { DEFAULT_RULES_SUCCESS_EMPTY_MOCK },
): IRepoRule {
    override suspend fun createRule(request: DbRuleRequest): IDbRuleResponse {
        return invokeCreateRule(request)
    }

    override suspend fun readRule(request: DbRuleIdRequest): IDbRuleResponse {
        return invokeReadRule(request)
    }

    override suspend fun updateRule(request: DbRuleRequest): IDbRuleResponse {
        return invokeUpdateRule(request)
    }

    override suspend fun deleteRule(request: DbRuleIdRequest): IDbRuleResponse {
        return invokeDeleteRule(request)
    }

    override suspend fun searchRule(request: DbRuleFilterRequest): IDbRulesResponse {
        return invokeSearchRule(request)
    }

    companion object {
        val DEFAULT_RULE_SUCCESS_EMPTY_MOCK = IDbRuleResponse.DbRuleSuccessResponse(AppRule())
        val DEFAULT_RULES_SUCCESS_EMPTY_MOCK = IDbRulesResponse.DbRulesSuccessResponse(emptyList())
    }
}