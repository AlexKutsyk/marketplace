package com.otus.otuskotlin.skillGrader.tests.e2e.be.scenarios.v1.base

import com.otus.otuskotlin.skillGrader.project.api.v1.models.Grade
import com.otus.otuskotlin.skillGrader.project.api.v1.models.RuleCreateObject

val someCreateRule = RuleCreateObject(
    name = "test rule",
    grade = Grade.A1,
    minGrammarPercent = 30,
    minLexiconsPercent = 30,
    minListeningPercent = 30,
    timeWindowDays = 0,
    priority = 1
)