package com.otus.otuskotlin.skillGrader.project.common.repo

interface IRepoRule {
    suspend fun createRule(request: DbRuleRequest): IDbRuleResponse
    suspend fun readRule(request: DbRuleIdRequest): IDbRuleResponse
    suspend fun updateRule(request: DbRuleRequest): IDbRuleResponse
    suspend fun deleteRule(request: DbRuleIdRequest): IDbRuleResponse
    suspend fun searchRule(request: DbRuleFilterRequest): IDbRulesResponse

    companion object {
        val NONE = object : IRepoRule {
            override suspend fun createRule(request: DbRuleRequest): IDbRuleResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun readRule(request: DbRuleIdRequest): IDbRuleResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun updateRule(request: DbRuleRequest): IDbRuleResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun deleteRule(request: DbRuleIdRequest): IDbRuleResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun searchRule(request: DbRuleFilterRequest): IDbRulesResponse {
                throw NotImplementedError("Must not be used")
            }
        }
    }
}