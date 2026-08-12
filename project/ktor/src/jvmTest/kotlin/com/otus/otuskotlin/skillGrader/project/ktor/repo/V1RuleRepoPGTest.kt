package com.otus.otuskotlin.skillGrader.project.ktor.repo

import com.otus.otuskotlin.skillGrader.api.v1.models.RuleRequestDebugMode
import com.otus.otuskotlin.skillGrader.project.common.AppCorSettings
import com.otus.otuskotlin.skillGrader.project.common.repo.IRepoRule
import com.otus.otuskotlin.skillGrader.project.ktor.ServiceSettings
import com.otus.otuskotlin.skillGrader.repo.pgsqlx4k.RepoRuleSql
import kotlin.test.BeforeTest

open class V1RuleRepoPGTest : RuleRepoBaseTest() {

    private val cleanRepo = RuleRepoPGTest.repoUnderTestContainer()

    private fun makeAppSettings(repo: IRepoRule) = ServiceSettings(
        corSettings = AppCorSettings(
            repoTest = repo,
            repoProd = repo,
        )
    )

    @BeforeTest
    fun beforeTest() {
        val pgRepo = cleanRepo.repo as RepoRuleSql
        pgRepo.clear()
    }

    override val workMode = RuleRequestDebugMode.PROD

    override val appSettingsCreate: ServiceSettings by lazy {
        makeAppSettings(
            repo = RuleRepoPGTest.repoUnderTestContainer(
                randomUuid = { uuidNew }
            )
        )
    }

    override val appSettingsRead: ServiceSettings by lazy {
        makeAppSettings(
            repo = RuleRepoPGTest.repoUnderTestContainer(
                initObjects = listOf(initRule),
                randomUuid = { uuidNew }
            )
        )
    }

    override val appSettingsUpdate: ServiceSettings by lazy {
        makeAppSettings(
            repo = RuleRepoPGTest.repoUnderTestContainer(
                initObjects = listOf(initRule),
                randomUuid = { uuidNew }
            )
        )
    }

    override val appSettingsDelete: ServiceSettings by lazy {
        makeAppSettings(
            repo = RuleRepoPGTest.repoUnderTestContainer(
                initObjects = listOf(initRule),
                randomUuid = { uuidNew },
            )
        )
    }

    override val appSettingsSearch: ServiceSettings by lazy {
        makeAppSettings(
            repo = RuleRepoPGTest.repoUnderTestContainer(
                initObjects = listOf(initRule),
                randomUuid = { uuidNew },
            )
        )
    }
}