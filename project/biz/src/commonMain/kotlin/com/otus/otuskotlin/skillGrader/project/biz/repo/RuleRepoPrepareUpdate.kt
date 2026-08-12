package com.otus.otuskotlin.skillGrader.project.biz.repo

import ICorChainBuilder
import com.otus.otuskotlin.skillGrader.project.common.AppContext
import com.otus.otuskotlin.skillGrader.project.common.models.AppState
import worker

fun ICorChainBuilder<AppContext>.repoPrepareUpdate(title: String) = worker {
    this.title = title
    description = "Готовим данные к сохранению в БД: совмещаем данные, прочитанные из БД, " +
            "и данные, полученные от пользователя"
    on { state == AppState.RUNNING }
    handle {
        ruleRepoPrepare = ruleRepoRead.deepCopy().apply {
            this.name = ruleValidated.name
            grade = ruleValidated.grade
            minGrammarPercent = ruleValidated.minGrammarPercent
            minLexiconsPercent = ruleValidated.minLexiconsPercent
            minListeningPercent = ruleValidated.minListeningPercent
            timeWindowDays = ruleValidated.timeWindowDays
            priority = ruleValidated.priority
            lock = ruleValidated.lock
        }
    }
}