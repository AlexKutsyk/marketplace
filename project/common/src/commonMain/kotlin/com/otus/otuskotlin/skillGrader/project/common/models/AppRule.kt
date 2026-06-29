package com.otus.otuskotlin.skillGrader.project.common.models

data class AppRule(
    var id: AppRuleId = AppRuleId.NONE,
    var name: String = "",
    var grade: AppGrade = AppGrade.NONE,
    var minGrammarPercent: Int = 0,
    var minLexiconsPercent: Int = 0,
    var minListeningPercent: Int = 0,
    var timeWindowDays: Int = 0,
    var priority: Int = 0,
    var lock: AppRuleLock = AppRuleLock.NONE,
    val permissionsClient: MutableSet<AppRulePermissionClient> = mutableSetOf(),
) {
    fun deepCopy(): AppRule = copy(
        permissionsClient = permissionsClient.toMutableSet(),
    )

    fun isEmpty() = this == NONE

    companion object {
        private val NONE = AppRule()
    }
}