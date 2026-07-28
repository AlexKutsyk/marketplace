package com.otus.otuskotlin.skillGrader.repo.pgsqlx4k

data class SqlProperties(
    val host: String = "localhost",
    val port: Int = 5432,
    val user: String = "postgres",
    val password: String = "skill-grader-pass",
    val database: String = "skill-grader-rules",
    val schema: String = "public",
    val table: String = "rules",
    val maxConnections: Int = 2,
) {
    val url: String
        get() = "postgresql://$host:$port/$database"
}