package com.otus.otuskotlin.skillGrader.repo.inmemory

import com.otus.otuskotlin.skillGrader.project.common.models.AppGrade
import com.otus.otuskotlin.skillGrader.project.common.models.AppRule
import com.otus.otuskotlin.skillGrader.project.common.models.AppRuleId
import com.otus.otuskotlin.skillGrader.project.common.models.AppRuleLock

data class RuleEntity(
    val id: String? = null,
    val name: String? = null,
    val grade: String? = null,
    val minGrammarPercent: String? = null,
    val minLexiconsPercent: String? = null,
    val minListeningPercent: String? = null,
    val timeWindowDays: String? = null,
    val priority: String? = null,
    val lock: String? = null,
) {
    constructor(model: AppRule) : this(
        id = model.id.asString().takeIf { it.isNotBlank() },
        name = model.name.takeIf { it.isNotBlank() },
        grade = model.grade.takeIf { it != AppGrade.NONE }?.name,
        minGrammarPercent = model.minGrammarPercent.takeIf { it in 0..100 }.toString(),
        minLexiconsPercent = model.minLexiconsPercent.takeIf { it in 0..100 }.toString(),
        minListeningPercent = model.minListeningPercent.takeIf { it in 0..100 }.toString(),
        timeWindowDays = model.timeWindowDays.takeIf { it in 0..365 }.toString(),
        priority = model.priority.takeIf { it in 0..100 }.toString(),
        lock = model.lock.asString().takeIf { it.isNotBlank() }
        // Не нужно сохранять permissions, потому что он ВЫЧИСЛЯЕМЫЙ, а не хранимый
    )

    fun toInternal() = AppRule(
        id = id?.let { AppRuleId(it) } ?: AppRuleId.NONE,
        name = name ?: "",
        grade = grade?.let { AppGrade.valueOf(it) } ?: AppGrade.NONE,
        minGrammarPercent = minGrammarPercent?.toInt() ?: 0,
        minLexiconsPercent = minLexiconsPercent?.toInt() ?: 0,
        minListeningPercent = minListeningPercent?.toInt() ?: 0,
        timeWindowDays = timeWindowDays?.toInt() ?: 0,
        priority = priority?.toInt() ?: 0,
        lock = lock?.let { AppRuleLock(it) } ?: AppRuleLock.NONE,
    )
}