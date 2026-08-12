package com.otus.otuskotlin.skillGrader.project.common.repo

import com.otus.otuskotlin.skillGrader.project.common.helpers.errorSystem

abstract class RuleRepoBase: IRepoRule {

    protected suspend fun tryRuleMethod(block: suspend () -> IDbRuleResponse) = try {
        block()
    } catch (e: Throwable) {
        IDbRuleResponse.DbRuleErrorResponse(errorSystem("methodException", e = e))
    }

    protected suspend fun tryRulesMethod(block: suspend () -> IDbRulesResponse) = try {
        block()
    } catch (e: Throwable) {
        IDbRulesResponse.DbRulesErrorResponse(errorSystem("methodException", e = e))
    }

}
