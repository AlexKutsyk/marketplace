package com.otus.otuskotlin.skillGrader.project.ktor.plugins

import com.otus.otuskotlin.skillGrader.project.common.repo.IRepoRule
import com.otus.otuskotlin.skillGrader.project.ktor.configs.PostgresConfig
import com.otus.otuskotlin.skillGrader.repo.inmemory.RuleRepoInMemory
import com.otus.otuskotlin.skillGrader.repo.pgsqlx4k.RepoRuleSql
import com.otus.otuskotlin.skillGrader.repo.pgsqlx4k.SqlProperties
import io.ktor.server.application.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

expect fun Application.getDatabaseConf(type: RuleDbType): IRepoRule

enum class RuleDbType(val confName: String) {
    PROD("prod"),
    TEST("test")
}

fun Application.initInMemory(): IRepoRule {
    val ttlSetting = environment.config.propertyOrNull("db.prod")?.getString()?.let {
        Duration.parse(it)
    }
    return RuleRepoInMemory(ttl = ttlSetting ?: 10.minutes)
}

/**
 * Postgres инициализация — единая для всех платформ.
 * RepoRuleSql и PostgresConfig мультиплатформенные.
 */
fun Application.initPostgres(): IRepoRule {
    val config = PostgresConfig(environment.config)
    return RepoRuleSql(
        properties = SqlProperties(
            host = config.host,
            port = config.port,
            user = config.user,
            password = config.password,
            schema = config.schema,
            database = config.database,
        ),
    )
}