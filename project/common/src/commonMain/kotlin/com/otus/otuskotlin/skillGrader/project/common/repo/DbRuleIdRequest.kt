package com.otus.otuskotlin.skillGrader.project.common.repo

import com.otus.otuskotlin.skillGrader.project.common.models.AppRule
import com.otus.otuskotlin.skillGrader.project.common.models.AppRuleId
import com.otus.otuskotlin.skillGrader.project.common.models.AppRuleLock

data class DbRuleIdRequest(
    val id: AppRuleId,
    val lock: AppRuleLock = AppRuleLock.NONE,
) {
    constructor(rule: AppRule) : this(rule.id, rule.lock)
}