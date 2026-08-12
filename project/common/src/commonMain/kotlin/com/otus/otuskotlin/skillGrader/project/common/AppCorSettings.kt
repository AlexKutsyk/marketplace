package com.otus.otuskotlin.skillGrader.project.common

import com.otus.otuskotlin.skillGrader.logging.common.LoggerProvider
import com.otus.otuskotlin.skillGrader.project.common.repo.IRepoRule
import com.otus.otuskotlin.skillGrader.project.common.ws.IWsSessionRepo

data class AppCorSettings(
    val loggerProvider: LoggerProvider = LoggerProvider(),
    val wsSessions: IWsSessionRepo = IWsSessionRepo.NONE,
    val repoStub: IRepoRule = IRepoRule.NONE,
    val repoTest: IRepoRule = IRepoRule.NONE,
    val repoProd: IRepoRule = IRepoRule.NONE,
) {
    companion object {
        val NONE = AppCorSettings()
    }
}