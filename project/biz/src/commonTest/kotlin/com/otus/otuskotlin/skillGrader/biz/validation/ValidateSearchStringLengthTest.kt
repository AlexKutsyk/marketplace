package com.otus.otuskotlin.skillGrader.biz.validation

import com.otus.otuskotlin.skillGrader.project.biz.validation.validateSearchStringLength
import com.otus.otuskotlin.skillGrader.project.common.AppContext
import com.otus.otuskotlin.skillGrader.project.common.models.AppRuleFilter
import com.otus.otuskotlin.skillGrader.project.common.models.AppState
import kotlinx.coroutines.test.runTest
import rootChain
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidateSearchStringLengthTest {
    @Test
    fun emptyString() = runTest {
        val context = AppContext(state = AppState.RUNNING, ruleFilterValidating = AppRuleFilter(searchString = ""))
        chain.exec(context)
        assertEquals(AppState.RUNNING, context.state)
        assertEquals(0, context.errors.size)
    }

    @Test
    fun blankString() = runTest {
        val ctx = AppContext(state = AppState.RUNNING, ruleFilterValidating = AppRuleFilter(searchString = "  "))
        chain.exec(ctx)
        assertEquals(AppState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun shortString() = runTest {
        val ctx = AppContext(state = AppState.RUNNING, ruleFilterValidating = AppRuleFilter(searchString = "12"))
        chain.exec(ctx)
        assertEquals(AppState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-searchString-tooShort", ctx.errors.first().code)
    }

    @Test
    fun normalString() = runTest {
        val ctx = AppContext(state = AppState.RUNNING, ruleFilterValidating = AppRuleFilter(searchString = "123"))
        chain.exec(ctx)
        assertEquals(AppState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun longString() = runTest {
        val ctx = AppContext(state = AppState.RUNNING, ruleFilterValidating = AppRuleFilter(searchString = "12".repeat(51)))
        chain.exec(ctx)
        assertEquals(AppState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-searchString-tooLong", ctx.errors.first().code)
    }

    companion object {
        val chain = rootChain {
            validateSearchStringLength("")
        }.build()
    }
}