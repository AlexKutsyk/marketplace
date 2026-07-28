package com.otus.otuskotlin.skillGrader.project.repo.tests

import com.otus.otuskotlin.skillGrader.project.common.models.AppGrade
import com.otus.otuskotlin.skillGrader.project.common.models.AppRule
import com.otus.otuskotlin.skillGrader.project.common.models.AppRuleId
import com.otus.otuskotlin.skillGrader.project.common.models.AppRuleLock

abstract class BaseInitRules(private val operation: String) : IInitObjects<AppRule> {
    open val lockOld: AppRuleLock = AppRuleLock("20000000-0000-0000-0000-000000000001")
    open val lockBad: AppRuleLock = AppRuleLock("20000000-0000-0000-0000-000000000009")

    fun createInitTestModel(
        suf: String,
        grade: AppGrade = AppGrade.A1,
        lock: AppRuleLock = lockOld,
    ) = AppRule(
        id = AppRuleId("rule-repo-$operation-$suf"),
        name = "$suf ${grade.name} stub",
        grade = grade,
        minGrammarPercent = 5,
        minLexiconsPercent = 10,
        minListeningPercent = 15,
        timeWindowDays = 14,
        priority = 20,
        lock = lock,
    )
}