package com.otus.otuskotlin.skillGrader.project.common.models

data class AppRuleFilter(
    var searchString: String = "",
    var grade: AppGrade = AppGrade.NONE
) {
    fun deepCopy(): AppRuleFilter = copy()

    fun isEmpty() = this == NONE

    companion object {
        private val NONE = AppRuleFilter()
    }
}