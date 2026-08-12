package com.otus.otuskotlin.skillGrader.project.mappers.v1

import com.otus.otuskotlin.skillGrader.project.api.v1.models.RuleCreateObject
import com.otus.otuskotlin.skillGrader.project.api.v1.models.RuleDeleteObject
import com.otus.otuskotlin.skillGrader.project.api.v1.models.RuleReadObject
import com.otus.otuskotlin.skillGrader.project.api.v1.models.RuleUpdateObject
import com.otus.otuskotlin.skillGrader.project.common.models.AppRule
import com.otus.otuskotlin.skillGrader.project.common.models.AppRuleLock

fun AppRule.toTransportCreateRule() = RuleCreateObject(
    name = name,
    grade = grade.toTransportRule(),
    minGrammarPercent = minGrammarPercent,
    minLexiconsPercent = minLexiconsPercent,
    minListeningPercent = minListeningPercent,
    timeWindowDays = timeWindowDays,
    priority = priority
)

fun AppRule.toTransportReadRule() = RuleReadObject(
    id = id.toTransportRule()
)

fun AppRule.toTransportUpdateRule() = RuleUpdateObject(
    id = id.toTransportRule(),
    name = name,
    grade = grade.toTransportRule(),
    minGrammarPercent = minGrammarPercent,
    minLexiconsPercent = minLexiconsPercent,
    minListeningPercent = minListeningPercent,
    timeWindowDays = timeWindowDays,
    priority = priority,
    lock = lock.toTransportRule(),
)

internal fun AppRuleLock.toTransportRule() = takeIf { it != AppRuleLock.NONE }?.asString()

fun AppRule.toTransportDeleteRule() = RuleDeleteObject(
    id = id.toTransportRule(),
    lock = lock.toTransportRule(),
)