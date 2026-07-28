package com.otus.otuskotlin.skillGrader.project.repo.tests

import com.otus.otuskotlin.skillGrader.project.common.models.AppRule
import com.otus.otuskotlin.skillGrader.project.common.repo.DbRuleFilterRequest
import com.otus.otuskotlin.skillGrader.project.common.repo.DbRuleIdRequest
import com.otus.otuskotlin.skillGrader.project.common.repo.DbRuleRequest
import com.otus.otuskotlin.skillGrader.project.common.repo.IDbRuleResponse
import com.otus.otuskotlin.skillGrader.project.common.repo.IDbRulesResponse
import com.otus.otuskotlin.skillGrader.project.stubs.AppRuleStub
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class RuleRepositoryMockTest {
    private val repo = RuleRepositoryMock(
        invokeCreateRule = { IDbRuleResponse.DbRuleSuccessResponse(AppRuleStub.prepareResult { name = "create" }) },
        invokeReadRule = { IDbRuleResponse.DbRuleSuccessResponse(AppRuleStub.prepareResult { name = "read" }) },
        invokeUpdateRule = { IDbRuleResponse.DbRuleSuccessResponse(AppRuleStub.prepareResult { name = "update" }) },
        invokeDeleteRule = { IDbRuleResponse.DbRuleSuccessResponse(AppRuleStub.prepareResult { name = "delete" }) },
        invokeSearchRule = {
            IDbRulesResponse.DbRulesSuccessResponse(listOf(AppRuleStub.prepareResult { name = "search" }))
        },
    )

    @Test
    fun mockCreate() = runTest {
        val result = repo.createRule(DbRuleRequest(AppRule()))
        assertIs<IDbRuleResponse.DbRuleSuccessResponse>(result)
        assertEquals("create", result.data.name)
    }

    @Test
    fun mockRead() = runTest {
        val result = repo.readRule(DbRuleIdRequest(AppRule()))
        assertIs<IDbRuleResponse.DbRuleSuccessResponse>(result)
        assertEquals("read", result.data.name)
    }

    @Test
    fun mockUpdate() = runTest {
        val result = repo.updateRule(DbRuleRequest(AppRule()))
        assertIs<IDbRuleResponse.DbRuleSuccessResponse>(result)
        assertEquals("update", result.data.name)
    }

    @Test
    fun mockDelete() = runTest {
        val result = repo.deleteRule(DbRuleIdRequest(AppRule()))
        assertIs<IDbRuleResponse.DbRuleSuccessResponse>(result)
        assertEquals("delete", result.data.name)
    }

    @Test
    fun mockSearch() = runTest {
        val result = repo.searchRule(DbRuleFilterRequest())
        assertIs<IDbRulesResponse.DbRulesSuccessResponse>(result)
        assertEquals("search", result.data.first().name)
    }
}