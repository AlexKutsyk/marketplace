package com.otus.otuskotlin.skillGrader.biz.validation

import com.otus.otuskotlin.skillGrader.project.common.AppContext
import com.otus.otuskotlin.skillGrader.project.common.models.AppCommand
import com.otus.otuskotlin.skillGrader.project.common.models.AppRuleFilter
import com.otus.otuskotlin.skillGrader.project.common.models.AppState
import com.otus.otuskotlin.skillGrader.project.common.models.AppWorkMode
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class BizValidationSearchTest: BaseBizValidationTest() {
    override val command = AppCommand.SEARCH

    @Test
    fun correctEmpty() = runTest {
        val context = AppContext(
            command = command,
            state = AppState.NONE,
            workMode = AppWorkMode.TEST,
            ruleFilterRequest = AppRuleFilter()
        )
        processor.execute(context)
        assertEquals(0, context.errors.size)
        assertNotEquals(AppState.FAILING, context.state)
    }
}