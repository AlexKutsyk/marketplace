package com.otus.otuskotlin.skillGrader.project.common.repo.exceptions

import com.otus.otuskotlin.skillGrader.project.common.models.AppRuleId

open class RepoRuleException(
    @Suppress("unused")
    val adId: AppRuleId,
    msg: String,
): RepoException(msg)
