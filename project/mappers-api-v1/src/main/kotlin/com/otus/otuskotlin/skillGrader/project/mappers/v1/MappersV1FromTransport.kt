package com.otus.otuskotlin.skillGrader.project.mappers.v1

import com.otus.otuskotlin.skillGrader.project.mappers.v1.exceptions.UnknownRequestClass
import com.otus.otuskotlin.skillGrader.api.v1.models.Grade
import com.otus.otuskotlin.skillGrader.api.v1.models.IRequest
import com.otus.otuskotlin.skillGrader.api.v1.models.RuleCreateObject
import com.otus.otuskotlin.skillGrader.api.v1.models.RuleCreateRequest
import com.otus.otuskotlin.skillGrader.api.v1.models.RuleDebug
import com.otus.otuskotlin.skillGrader.api.v1.models.RuleDeleteObject
import com.otus.otuskotlin.skillGrader.api.v1.models.RuleDeleteRequest
import com.otus.otuskotlin.skillGrader.api.v1.models.RuleReadObject
import com.otus.otuskotlin.skillGrader.api.v1.models.RuleReadRequest
import com.otus.otuskotlin.skillGrader.api.v1.models.RuleRequestDebugMode
import com.otus.otuskotlin.skillGrader.api.v1.models.RuleRequestDebugStubs
import com.otus.otuskotlin.skillGrader.api.v1.models.RuleSearchFilter
import com.otus.otuskotlin.skillGrader.api.v1.models.RuleSearchRequest
import com.otus.otuskotlin.skillGrader.api.v1.models.RuleUpdateObject
import com.otus.otuskotlin.skillGrader.api.v1.models.RuleUpdateRequest
import com.otus.otuskotlin.skillGrader.project.common.AppContext
import com.otus.otuskotlin.skillGrader.project.common.models.AppCommand
import com.otus.otuskotlin.skillGrader.project.common.models.AppGrade
import com.otus.otuskotlin.skillGrader.project.common.models.AppRule
import com.otus.otuskotlin.skillGrader.project.common.models.AppRuleFilter
import com.otus.otuskotlin.skillGrader.project.common.models.AppRuleId
import com.otus.otuskotlin.skillGrader.project.common.models.AppRuleLock
import com.otus.otuskotlin.skillGrader.project.common.models.AppWorkMode
import com.otus.otuskotlin.skillGrader.project.common.stubs.AppStubs

fun AppContext.fromTransport(request: IRequest) = when (request) {
    is RuleCreateRequest -> fromTransport(request)
    is RuleReadRequest -> fromTransport(request)
    is RuleUpdateRequest -> fromTransport(request)
    is RuleDeleteRequest -> fromTransport(request)
    is RuleSearchRequest -> fromTransport(request)
    else -> throw UnknownRequestClass(request.javaClass)
}

private fun String?.toRuleId() = this?.let {
    AppRuleId(
        it
    )
} ?: AppRuleId.NONE
private fun String?.toRuleLock() = this?.let {
    AppRuleLock(
        it
    )
} ?: AppRuleLock.NONE

private fun RuleDebug?.transportToWorkMode(): AppWorkMode = when (this?.mode) {
    RuleRequestDebugMode.PROD -> AppWorkMode.PROD
    RuleRequestDebugMode.TEST -> AppWorkMode.TEST
    RuleRequestDebugMode.STUB -> AppWorkMode.STUB
    null -> AppWorkMode.PROD
}

private fun RuleDebug?.transportToStubCase(): AppStubs = when (this?.stub) {
    RuleRequestDebugStubs.SUCCESS -> AppStubs.SUCCESS
    RuleRequestDebugStubs.NOT_FOUND -> AppStubs.NOT_FOUND
    RuleRequestDebugStubs.BAD_ID -> AppStubs.BAD_ID
    RuleRequestDebugStubs.BAD_NAME -> AppStubs.BAD_NAME
    RuleRequestDebugStubs.BAD_MIN_GRAMMAR_PERCENT -> AppStubs.BAD_MIN_GRAMMAR_PERCENT
    RuleRequestDebugStubs.BAD_MIN_LEXICONS_PERCENT -> AppStubs.BAD_MIN_LEXICONS_PERCENT
    RuleRequestDebugStubs.BAD_MIN_LISTENING_PERCENT -> AppStubs.BAD_MIN_LISTENING_PERCENT
    RuleRequestDebugStubs.BAD_TIME_WINDOW_DAYS -> AppStubs.BAD_TIME_WINDOW_DAYS
    RuleRequestDebugStubs.BAD_PRIORITY -> AppStubs.BAD_PRIORITY
    RuleRequestDebugStubs.CAN_NOT_DELETE -> AppStubs.CANNOT_DELETE
    RuleRequestDebugStubs.BED_SEARCH_STRING -> AppStubs.BAD_SEARCH_STRING
    null -> AppStubs.NONE
}

fun AppContext.fromTransport(request: RuleReadRequest) {
    command = AppCommand.READ
    ruleRequest = request.requestRule.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun RuleReadObject?.toInternal(): AppRule = if (this != null) {
    AppRule(
        id = id.toRuleId()
    )
} else {
    AppRule()
}

fun AppContext.fromTransport(request: RuleCreateRequest) {
    command = AppCommand.CREATE
    ruleRequest = request.requestRule?.toInternal() ?: AppRule()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun AppContext.fromTransport(request: RuleUpdateRequest) {
    command = AppCommand.UPDATE
    ruleRequest = request.requestRule?.toInternal() ?: AppRule()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun AppContext.fromTransport(request: RuleDeleteRequest) {
    command = AppCommand.DELETE
    ruleRequest = request.requestRule.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun RuleDeleteObject?.toInternal(): AppRule = if (this != null) {
    AppRule(
        id = id.toRuleId(),
        lock = lock.toRuleLock(),
    )
} else {
    AppRule()
}

fun AppContext.fromTransport(request: RuleSearchRequest) {
    command = AppCommand.SEARCH
    ruleFilterRequest = request.ruleFilter.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun RuleSearchFilter?.toInternal(): AppRuleFilter =
    AppRuleFilter(
        searchString = this?.searchString ?: ""
    )

private fun RuleCreateObject.toInternal(): AppRule =
    AppRule(
        name = name ?: "",
        grade = grade.fromTransport(),
        minGrammarPercent = minGrammarPercent ?: 0,
        minLexiconsPercent = minLexiconsPercent ?: 0,
        minListeningPercent = minListeningPercent ?: 0,
        timeWindowDays = timeWindowDays ?: 0,
        priority = priority ?: 0
    )

private fun RuleUpdateObject.toInternal(): AppRule =
    AppRule(
        id = id.toRuleId(),
        name = name ?: "",
        grade = grade.fromTransport(),
        minGrammarPercent = minGrammarPercent ?: 0,
        minLexiconsPercent = minLexiconsPercent ?: 0,
        minListeningPercent = minListeningPercent ?: 0,
        timeWindowDays = timeWindowDays ?: 0,
        priority = priority ?: 0,
        lock = lock.toRuleLock(),
    )

internal fun Grade?.fromTransport(): AppGrade =
    when(this) {
        Grade.A1 -> AppGrade.A1
        Grade.A2 -> AppGrade.A2
        Grade.B1 -> AppGrade.B1
        Grade.B2 -> AppGrade.B2
        Grade.C1 -> AppGrade.C1
        Grade.C2 -> AppGrade.C2
        null -> AppGrade.NONE
    }