package com.otus.otuskotlin.skillGrader.project.common.models

data class AppRuleFilter(
    var searchString: String = "",
) {
    fun deepCopy(): AppRuleFilter = copy()

    fun isEmpty() = this == NONE

    companion object {
        private val NONE = AppRuleFilter()
    }
}