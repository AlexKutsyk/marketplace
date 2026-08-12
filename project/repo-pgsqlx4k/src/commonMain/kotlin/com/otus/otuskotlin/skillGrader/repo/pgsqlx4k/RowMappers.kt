package com.otus.otuskotlin.skillGrader.repo.pgsqlx4k

import com.otus.otuskotlin.skillGrader.project.common.models.AppGrade
import com.otus.otuskotlin.skillGrader.project.common.models.AppRule
import com.otus.otuskotlin.skillGrader.project.common.models.AppRuleId
import com.otus.otuskotlin.skillGrader.project.common.models.AppRuleLock
import io.github.smyrgeorge.sqlx4k.RowMapper
import io.github.smyrgeorge.sqlx4k.ValueEncoderRegistry
import io.github.smyrgeorge.sqlx4k.ResultSet

object RuleRowMapper : RowMapper<AppRule> {
    override fun map(row: ResultSet.Row, converters: ValueEncoderRegistry): AppRule {
        return AppRule(
            id = AppRuleId(row.get(SqlFields.ID).asString()),
            name = row.get(SqlFields.NAME).asString(),
            grade = mapGrade(row.get(SqlFields.GRADE).asString()),
            minGrammarPercent = row.get(SqlFields.MIN_GRAMMAR_PERCENT).asString().toInt(),
            minLexiconsPercent = row.get(SqlFields.MIN_LEXICON_PERCENT).asString().toInt(),
            minListeningPercent = row.get(SqlFields.MIN_LISTENING_PERCENT).asString().toInt(),
            timeWindowDays = row.get(SqlFields.TIME_WINDOW_DAYS).asString().toInt(),
            priority = row.get(SqlFields.PRIORITY).asString().toInt(),
            lock = AppRuleLock(row.get(SqlFields.LOCK).asString()),
        )
    }

    private fun mapGrade(value: String): AppGrade = when (value) {
        SqlFields.GRADE_TYPE_A1 -> AppGrade.A1
        SqlFields.GRADE_TYPE_A2 -> AppGrade.A2
        SqlFields.GRADE_TYPE_B1 -> AppGrade.B1
        SqlFields.GRADE_TYPE_B2 -> AppGrade.B2
        SqlFields.GRADE_TYPE_C1 -> AppGrade.C1
        SqlFields.GRADE_TYPE_C2 -> AppGrade.C2
        else -> AppGrade.NONE
    }
}