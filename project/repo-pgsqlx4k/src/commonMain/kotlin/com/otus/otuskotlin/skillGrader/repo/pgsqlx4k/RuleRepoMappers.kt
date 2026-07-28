package com.otus.otuskotlin.skillGrader.repo.pgsqlx4k

import com.otus.otuskotlin.skillGrader.project.common.models.AppGrade
import com.otus.otuskotlin.skillGrader.project.common.models.AppRule
import io.github.smyrgeorge.sqlx4k.Statement

internal fun Statement.bindRule(rule: AppRule): Statement = apply {
    bind(SqlFields.ID, rule.id.asString())
    bind(SqlFields.NAME, rule.name)
    bind(SqlFields.GRADE, rule.grade.toDbString())
    bind(SqlFields.MIN_GRAMMAR_PERCENT, rule.minGrammarPercent)
    bind(SqlFields.MIN_LEXICON_PERCENT, rule.minLexiconsPercent)
    bind(SqlFields.MIN_LISTENING_PERCENT, rule.minListeningPercent)
    bind(SqlFields.TIME_WINDOW_DAYS, rule.timeWindowDays)
    bind(SqlFields.PRIORITY, rule.priority)
    bind(SqlFields.LOCK, rule.lock.asString())
}

internal fun AppGrade.toDbString(): String = when (this) {
    AppGrade.A1 -> SqlFields.GRADE_TYPE_A1
    AppGrade.A2 -> SqlFields.GRADE_TYPE_A2
    AppGrade.B1 -> SqlFields.GRADE_TYPE_B1
    AppGrade.B2 -> SqlFields.GRADE_TYPE_B2
    AppGrade.C1 -> SqlFields.GRADE_TYPE_C1
    AppGrade.C2 -> SqlFields.GRADE_TYPE_C2
    AppGrade.NONE -> throw Exception("Wrong value of rule Type. NONE is unsupported")
}