package com.otus.otuskotlin.skillGrader.project.stubs

import com.otus.otuskotlin.skillGrader.project.common.models.AppGrade
import com.otus.otuskotlin.skillGrader.project.common.models.AppRule
import com.otus.otuskotlin.skillGrader.project.common.models.AppRuleId
import com.otus.otuskotlin.skillGrader.project.stubs.AppRuleStubBase.RULE_STUB

object AppRuleStub {
    fun get(): AppRule = RULE_STUB.copy()

    fun prepareResult(block: AppRule.() -> Unit): AppRule = get().apply(block)

    fun prepareSearchList(filter: String, grade: AppGrade = AppGrade.A1) = listOf(
        appRule(RULE_STUB, "001", filter, AppGrade.A1),
        appRule(RULE_STUB, "002", filter, AppGrade.A2),
        appRule(RULE_STUB, "003", filter, AppGrade.B1),
        appRule(RULE_STUB, "004", filter, AppGrade.B2),
        appRule(RULE_STUB, "005", filter, AppGrade.C1),
        appRule(RULE_STUB, "006", filter, AppGrade.B2),
        appRule(RULE_STUB, "007", filter, AppGrade.B2),
        appRule(RULE_STUB, "008", filter, AppGrade.A1),
        appRule(RULE_STUB, "009", filter, AppGrade.C1),
    )

    private fun appRule(base: AppRule, id: String, filter: String, grade: AppGrade) = base.copy(
        id = AppRuleId(id),
        name = "$filter $grade",
        grade = grade,
    )
}