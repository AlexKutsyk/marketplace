package com.otus.otuskotlin.skillGrader.project.common.repo.exceptions

import com.otus.otuskotlin.skillGrader.project.common.models.AppRuleId
import com.otus.otuskotlin.skillGrader.project.common.models.AppRuleLock

class RepoConcurrencyException(id: AppRuleId, expectedLock: AppRuleLock, actualLock: AppRuleLock?) : RepoRuleException(
    id,
    "Expected lock is $expectedLock while actual lock in db is $actualLock"
)