package com.otus.otuskotlin.skillGrader.repo.common

import com.otus.otuskotlin.skillGrader.project.common.models.AppRule
import com.otus.otuskotlin.skillGrader.project.common.repo.IRepoRule

interface IRepoRuleInitializable: IRepoRule {
    fun save(ads: Collection<AppRule>) : Collection<AppRule>
}