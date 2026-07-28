package com.otus.otuskotlin.skillGrader.project.common.repo.exceptions

import com.otus.otuskotlin.skillGrader.project.common.models.AppRuleId

class RepoEmptyLockException(id: AppRuleId) : RepoRuleException(
    id,
    "Lock is empty in DB"
)