package com.otus.otuskotlin.skillGrader.project.common

import com.otus.otuskotlin.skillGrader.project.common.models.AppCommand
import com.otus.otuskotlin.skillGrader.project.common.models.AppError
import com.otus.otuskotlin.skillGrader.project.common.models.AppRequestId
import com.otus.otuskotlin.skillGrader.project.common.models.AppRule
import com.otus.otuskotlin.skillGrader.project.common.models.AppRuleFilter
import com.otus.otuskotlin.skillGrader.project.common.models.AppState
import com.otus.otuskotlin.skillGrader.project.common.models.AppWorkMode
import com.otus.otuskotlin.skillGrader.project.common.repo.IRepoRule
import com.otus.otuskotlin.skillGrader.project.common.stubs.AppStubs
import com.otus.otuskotlin.skillGrader.project.common.ws.IWsSession
import kotlin.time.Instant

data class AppContext(
    var command: AppCommand = AppCommand.NONE,
    var state: AppState = AppState.NONE,
    val errors: MutableList<AppError> = mutableListOf(),

    var corSettings: AppCorSettings = AppCorSettings(),
    var workMode: AppWorkMode = AppWorkMode.PROD,
    var stubCase: AppStubs = AppStubs.NONE,
    var wsSession: IWsSession = IWsSession.NONE,

    var requestId: AppRequestId = AppRequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var ruleRequest: AppRule = AppRule(),
    var ruleFilterRequest: AppRuleFilter = AppRuleFilter(),

    var ruleValidating: AppRule = AppRule(),
    var ruleFilterValidating: AppRuleFilter = AppRuleFilter(),

    var ruleValidated: AppRule = AppRule(),
    var ruleFilterValidated: AppRuleFilter = AppRuleFilter(),

    var ruleRepo: IRepoRule = IRepoRule.NONE,
    var ruleRepoRead: AppRule = AppRule(), // То, что прочитали из репозитория
    var ruleRepoPrepare: AppRule = AppRule(), // То, что готовим для сохранения в БД
    var ruleRepoDone: AppRule = AppRule(),  // Результат, полученный из БД
    var rulesRepoDone: MutableList<AppRule> = mutableListOf(),

    var ruleResponse: AppRule = AppRule(),
    var rulesResponse: MutableList<AppRule> = mutableListOf(),
)