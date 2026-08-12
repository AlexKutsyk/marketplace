package com.otus.otuskotlin.skillGrader.project.ktor.repo

import com.otus.otuskotlin.skillGrader.api.v1.models.RuleRequestDebugMode
import com.otus.otuskotlin.skillGrader.project.common.AppCorSettings
import com.otus.otuskotlin.skillGrader.project.common.repo.IRepoRule
import com.otus.otuskotlin.skillGrader.project.ktor.ServiceSettings
import com.otus.otuskotlin.skillGrader.repo.common.RuleRepoInitialized
import com.otus.otuskotlin.skillGrader.repo.inmemory.RuleRepoInMemory

class RuleRepoInmemoryTest : RuleRepoBaseTest() {
    override val workMode: RuleRequestDebugMode = RuleRequestDebugMode.TEST
    private fun makeAppSettings(repo: IRepoRule) = ServiceSettings(
        corSettings = AppCorSettings(
            repoTest = repo
        )
    )

    override val appSettingsCreate: ServiceSettings = makeAppSettings(
        repo = RuleRepoInitialized(RuleRepoInMemory(randomUuid = { uuidNew }))
    )
    override val appSettingsRead: ServiceSettings = makeAppSettings(
        repo = RuleRepoInitialized(
            RuleRepoInMemory(randomUuid = { uuidNew }),
            initObjects = listOf(initRule),
        )
    )
    override val appSettingsUpdate: ServiceSettings = makeAppSettings(
        repo = RuleRepoInitialized(
            RuleRepoInMemory(randomUuid = { uuidNew }),
            initObjects = listOf(initRule),
        )
    )
    override val appSettingsDelete: ServiceSettings = makeAppSettings(
        repo = RuleRepoInitialized(
            RuleRepoInMemory(randomUuid = { uuidNew }),
            initObjects = listOf(initRule),
        )
    )
    override val appSettingsSearch: ServiceSettings = makeAppSettings(
        repo = RuleRepoInitialized(
            RuleRepoInMemory(randomUuid = { uuidNew }),
            initObjects = listOf(initRule),
        )
    )
}