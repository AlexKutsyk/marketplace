package com.otus.otuskotlin.skillGrader.project.ktor.plugins

import com.otus.otuskotlin.skillGrader.project.common.repo.IRepoRule
import com.otus.otuskotlin.skillGrader.project.ktor.configs.ConfigPaths
import io.ktor.server.application.*

actual fun Application.getDatabaseConf(type: RuleDbType): IRepoRule {
    val dbSettingPath = "${ConfigPaths.repository}.${type.confName}"
    val dbSetting = environment.config.propertyOrNull(dbSettingPath)?.getString()?.lowercase()
    return when (dbSetting) {
        "in-memory", "inmemory", "memory", "mem" -> initInMemory()
        "postgres", "postgresql", "pg", "sql", "psql" -> initPostgres()
        else -> throw IllegalArgumentException(
            "$dbSettingPath must be set in application.yml to one of: 'inmemory', 'postgres'"
        )
    }
}