package com.otus.otuskotlin.skillGrader.project.ktor.repo

import com.benasher44.uuid.uuid4
import com.otus.otuskotlin.skillGrader.libs.sysenv.sysEnv
import com.otus.otuskotlin.skillGrader.project.common.models.AppRule
import com.otus.otuskotlin.skillGrader.repo.common.RuleRepoInitialized
import com.otus.otuskotlin.skillGrader.repo.pgsqlx4k.RepoRuleSql
import com.otus.otuskotlin.skillGrader.repo.pgsqlx4k.SqlProperties

/**
 * Вспомогательный объект для PG-тестов.
 * Порт читается через sysEnv — единый API для JVM и native.
 * Контейнер PostgreSQL управляется Gradle-задачами pgUp/pgDn.
 */
object RuleRepoPGTest {

    private val host get() = sysEnv("postgresHost") ?: "localhost"
    private val port get() = sysEnv("postgresPort")?.toInt() ?: 5432
    private val user get() = sysEnv("postgresUser") ?: "postgres"
    private val pass get() = sysEnv("postgresPass") ?: "skill-grader-pass"

    fun repoUnderTestContainer(
        initObjects: Collection<AppRule> = emptyList(),
        randomUuid: () -> String = { uuid4().toString() },
    ) = RuleRepoInitialized(
        repo = RepoRuleSql(
            SqlProperties(
                host = host,
                port = port,
                user = user,
                password = pass,
            ),
            randomUuid = randomUuid,
        ),
        initObjects = initObjects,
    )
}