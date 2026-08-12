package com.otus.otuskotlin.skillGrader.repo.pgsqlx4k

object SqlQueryBuilder {

    fun insert(dbName: String, cols: String): String = """
        INSERT INTO $dbName (
          ${SqlFields.ID.quoted()},
          ${SqlFields.NAME.quoted()},
          ${SqlFields.GRADE.quoted()},
          ${SqlFields.MIN_GRAMMAR_PERCENT.quoted()},
          ${SqlFields.MIN_LEXICON_PERCENT.quoted()},
          ${SqlFields.MIN_LISTENING_PERCENT.quoted()},
          ${SqlFields.TIME_WINDOW_DAYS.quoted()},
          ${SqlFields.PRIORITY.quoted()},
          ${SqlFields.LOCK.quoted()}
        ) VALUES (
          :${SqlFields.ID},
          :${SqlFields.NAME},
          CAST(:${SqlFields.GRADE} AS ${SqlFields.GRADE_TYPES}),
          :${SqlFields.MIN_GRAMMAR_PERCENT},
          :${SqlFields.MIN_LEXICON_PERCENT},
          :${SqlFields.MIN_LISTENING_PERCENT},
          :${SqlFields.TIME_WINDOW_DAYS},
          :${SqlFields.PRIORITY},
          :${SqlFields.LOCK}
        )
        RETURNING $cols
        """.trimIndent()

    fun read(dbName: String, cols: String): String = """
        SELECT $cols
        FROM $dbName
        WHERE ${SqlFields.ID.quoted()} = :${SqlFields.ID}
        """.trimIndent()

    fun update(dbName: String, cols: String): String = """
        WITH update_obj AS (
            UPDATE $dbName a
            SET ${SqlFields.NAME.quoted()} = :${SqlFields.NAME}
            , ${SqlFields.GRADE.quoted()} = CAST(:${SqlFields.GRADE} AS ${SqlFields.GRADE_TYPES})
            , ${SqlFields.MIN_GRAMMAR_PERCENT.quoted()} = :${SqlFields.MIN_GRAMMAR_PERCENT}
            , ${SqlFields.MIN_LEXICON_PERCENT.quoted()} = :${SqlFields.MIN_LEXICON_PERCENT}
            , ${SqlFields.MIN_LISTENING_PERCENT.quoted()} = :${SqlFields.MIN_LISTENING_PERCENT}
            , ${SqlFields.TIME_WINDOW_DAYS.quoted()} = :${SqlFields.TIME_WINDOW_DAYS}
            , ${SqlFields.PRIORITY.quoted()} = :${SqlFields.PRIORITY}
            , ${SqlFields.LOCK.quoted()} = :${SqlFields.LOCK}
            WHERE  a.${SqlFields.ID.quoted()} = :${SqlFields.ID}
            AND a.${SqlFields.LOCK.quoted()} = :${SqlFields.LOCK_OLD}
            RETURNING $cols
        ),
        select_obj AS (
            SELECT $cols FROM $dbName
            WHERE ${SqlFields.ID.quoted()} = :${SqlFields.ID}
        )
        (SELECT * FROM update_obj UNION ALL SELECT * FROM select_obj) LIMIT 1
        """.trimIndent()

    fun delete(dbName: String, cols: String): String = """
        WITH delete_obj AS (
            DELETE FROM $dbName a
            WHERE  a.${SqlFields.ID.quoted()} = :${SqlFields.ID}
            AND a.${SqlFields.LOCK.quoted()} = :${SqlFields.LOCK_OLD}
            RETURNING '${SqlFields.DELETE_OK}'
        )
        SELECT $cols, (SELECT * FROM delete_obj) as flag FROM $dbName
        WHERE ${SqlFields.ID.quoted()} = :${SqlFields.ID}
        """.trimIndent()

    fun search(dbName: String, cols: String, grade: Boolean, nameFilter: Boolean): String {
        val where = listOfNotNull(
            if (grade) "${SqlFields.GRADE.quoted()} = CAST(:${SqlFields.GRADE} AS ${SqlFields.GRADE_TYPES})" else null,
            if (nameFilter) "${SqlFields.NAME.quoted()} LIKE :${SqlFields.NAME}" else null,
        ).takeIf { it.isNotEmpty() }?.let { "WHERE ${it.joinToString(separator = " AND ")}" } ?: ""
        return """
            SELECT $cols
            FROM $dbName $where
            """.trimIndent()
    }

    fun clear(dbName: String): String = "DELETE FROM $dbName;"
}