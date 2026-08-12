package com.otus.otuskotlin.skillGrader.repo.pgsqlx4k

object SqlFields {
    const val ID = "id"
    const val NAME = "name"
    const val GRADE = "grade"
    const val MIN_GRAMMAR_PERCENT = "min_grammar_percent"
    const val MIN_LEXICON_PERCENT = "min_lexicons_percent"
    const val MIN_LISTENING_PERCENT = "min_listening_percent"
    const val TIME_WINDOW_DAYS = "time_window_days"
    const val PRIORITY = "priority"
    const val LOCK = "lock"
    const val LOCK_OLD = "lock_old"

    const val GRADE_TYPES = "rule_grade_type"
    const val GRADE_TYPE_A1 = "A1"
    const val GRADE_TYPE_A2 = "A2"
    const val GRADE_TYPE_B1 = "B1"
    const val GRADE_TYPE_B2 = "B2"
    const val GRADE_TYPE_C1 = "C1"
    const val GRADE_TYPE_C2 = "C2"

    const val DELETE_OK = "DELETE_OK"

    val allFields = listOf(
        ID,
        NAME,
        GRADE,
        MIN_GRAMMAR_PERCENT,
        MIN_LEXICON_PERCENT,
        MIN_LISTENING_PERCENT,
        TIME_WINDOW_DAYS,
        PRIORITY,
        LOCK
    )
}

internal fun String.quoted() = "\"$this\""