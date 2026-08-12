package com.otus.otuskotlin.skillGrader.project.common.repo

import com.otus.otuskotlin.skillGrader.project.common.models.AppGrade

data class DbRuleFilterRequest(
    val nameFilter: String = "",
    val grade: AppGrade = AppGrade.NONE
)