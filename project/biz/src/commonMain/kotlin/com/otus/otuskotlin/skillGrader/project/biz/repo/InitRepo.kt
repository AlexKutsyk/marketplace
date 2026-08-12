package com.otus.otuskotlin.skillGrader.project.biz.repo

import ICorChainBuilder
import com.otus.otuskotlin.skillGrader.project.biz.exceptions.AppDbNotConfiguredException
import com.otus.otuskotlin.skillGrader.project.common.AppContext
import com.otus.otuskotlin.skillGrader.project.common.helpers.errorSystem
import com.otus.otuskotlin.skillGrader.project.common.helpers.fail
import com.otus.otuskotlin.skillGrader.project.common.models.AppWorkMode
import com.otus.otuskotlin.skillGrader.project.common.repo.IRepoRule
import worker

fun ICorChainBuilder<AppContext>.initRepo(title: String) = worker() {
    this.title = title
    this.description = """
        Вычисление основного рабочего репозитория в зависимости от запрошенного режима работы.
    """.trimIndent()
    handle {
        ruleRepo = when (workMode) {
            AppWorkMode.TEST -> corSettings.repoTest
            AppWorkMode.STUB -> corSettings.repoStub
            else -> corSettings.repoProd
        }
        if (workMode != AppWorkMode.STUB && ruleRepo == IRepoRule.NONE) fail(
            errorSystem(
                violationCode = "dbNotConfigured",
                e = AppDbNotConfiguredException(workMode)
            )
        )
    }
}