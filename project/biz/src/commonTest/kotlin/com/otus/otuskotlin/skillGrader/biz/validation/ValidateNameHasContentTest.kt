package com.otus.otuskotlin.skillGrader.biz.validation

import com.otus.otuskotlin.skillGrader.project.biz.validation.validateNameHasContent
import com.otus.otuskotlin.skillGrader.project.common.AppContext
import com.otus.otuskotlin.skillGrader.project.common.models.AppRule
import com.otus.otuskotlin.skillGrader.project.common.models.AppRuleFilter
import com.otus.otuskotlin.skillGrader.project.common.models.AppState
import kotlinx.coroutines.test.runTest
import rootChain
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidateNameHasContentTest {
    @Test
    fun emptyString() = runTest {
        val ctx = AppContext(state = AppState.RUNNING, ruleValidating = AppRule(name = ""))
        chain.exec(ctx)
        assertEquals(AppState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun noContent() = runTest {
        val ctx = AppContext(state = AppState.RUNNING, ruleValidating = AppRule(name = "12!@#$%^&*()_+-="))
        chain.exec(ctx)
        assertEquals(AppState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-name-noContent", ctx.errors.first().code)
    }

    @Test
    fun normalString() = runTest {
        val ctx = AppContext(state = AppState.RUNNING, ruleFilterValidating = AppRuleFilter(searchString = "Ж"))
        chain.exec(ctx)
        assertEquals(AppState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    companion object {
        val chain = rootChain {
            validateNameHasContent("")
        }.build()
    }
}
