package com.otus.otuskotlin.skillGrader.repo.common

import com.otus.otuskotlin.skillGrader.project.common.models.AppRule

/**
 * Делегат для всех репозиториев, позволяющий инициализировать базу данных предзагруженными данными
 */
class RuleRepoInitialized(
    val repo: IRepoRuleInitializable,
    initObjects: Collection<AppRule> = emptyList(),
) : IRepoRuleInitializable by repo {
    @Suppress("unused")
    val initializedObjects: List<AppRule> = save(initObjects).toList()
}