package com.otus.otuskotlin.skillGrader.project.mappers.v1

import com.otus.otuskotlin.skillGrader.project.api.v1.models.Error
import com.otus.otuskotlin.skillGrader.project.api.v1.models.Grade
import com.otus.otuskotlin.skillGrader.project.api.v1.models.IResponse
import com.otus.otuskotlin.skillGrader.project.api.v1.models.ResponseResult
import com.otus.otuskotlin.skillGrader.project.api.v1.models.RuleCreateResponse
import com.otus.otuskotlin.skillGrader.project.api.v1.models.RuleDeleteResponse
import com.otus.otuskotlin.skillGrader.project.api.v1.models.RuleInitResponse
import com.otus.otuskotlin.skillGrader.project.api.v1.models.RulePermissions
import com.otus.otuskotlin.skillGrader.project.api.v1.models.RuleReadResponse
import com.otus.otuskotlin.skillGrader.project.api.v1.models.RuleResponseObject
import com.otus.otuskotlin.skillGrader.project.api.v1.models.RuleSearchResponse
import com.otus.otuskotlin.skillGrader.project.api.v1.models.RuleUpdateResponse
import com.otus.otuskotlin.skillGrader.project.common.exceptions.UnknownAppCommand
import com.otus.otuskotlin.skillGrader.project.common.AppContext
import com.otus.otuskotlin.skillGrader.project.common.models.AppCommand
import com.otus.otuskotlin.skillGrader.project.common.models.AppError
import com.otus.otuskotlin.skillGrader.project.common.models.AppGrade
import com.otus.otuskotlin.skillGrader.project.common.models.AppState
import com.otus.otuskotlin.skillGrader.project.common.models.AppRule
import com.otus.otuskotlin.skillGrader.project.common.models.AppRuleId
import com.otus.otuskotlin.skillGrader.project.common.models.AppRulePermissionClient

fun AppContext.toTransportRule(): IResponse = when (val cmd = command) {
    AppCommand.CREATE -> toTransportCreate()
    AppCommand.READ -> toTransportRead()
    AppCommand.UPDATE -> toTransportUpdate()
    AppCommand.DELETE -> toTransportDelete()
    AppCommand.SEARCH -> toTransportSearch()
    AppCommand.NONE -> throw UnknownAppCommand(cmd)
    AppCommand.INIT -> TODO()
    AppCommand.FINISH -> TODO()
}

fun AppContext.toTransportCreate() = RuleCreateResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    rule = ruleResponse.toTransportRule(),
)

fun AppContext.toTransportRead() = RuleReadResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    rule = ruleResponse.toTransportRule()
)

fun AppContext.toTransportUpdate() = RuleUpdateResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    rule = ruleResponse.toTransportRule()
)

fun AppContext.toTransportDelete() = RuleDeleteResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    rule = ruleResponse.toTransportRule()
)

fun AppContext.toTransportSearch() = RuleSearchResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    rules = rulesResponse.toTransportRule()
)

fun AppContext.toTransportInit() = RuleInitResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
)

fun List<AppRule>.toTransportRule(): List<RuleResponseObject>? = this
    .map { it.toTransportRule() }
    .toList()
    .takeIf { it.isNotEmpty() }

fun AppRule.toTransportRule(): RuleResponseObject = RuleResponseObject(
    id = id.toTransportRule(),
    name = name.takeIf { it.isNotBlank() },
    grade = grade.toTransportRule(),
    minGrammarPercent = minGrammarPercent,
    minLexiconsPercent = minLexiconsPercent,
    minListeningPercent = minListeningPercent,
    timeWindowDays = timeWindowDays,
    priority = priority,
    lock = lock.toTransportRule(),
    permissions = permissionsClient.toTransportRule()
)

internal fun AppRuleId.toTransportRule() = takeIf { it != AppRuleId.NONE }?.asString()

private fun Set<AppRulePermissionClient>.toTransportRule(): Set<RulePermissions>? = this
    .map { it.toTransportRule() }
    .toSet()
    .takeIf { it.isNotEmpty() }

private fun AppRulePermissionClient.toTransportRule() = when (this) {
    AppRulePermissionClient.READ -> RulePermissions.READ
    AppRulePermissionClient.UPDATE -> RulePermissions.UPDATE
    AppRulePermissionClient.DELETE -> RulePermissions.DELETE
}

private fun List<AppError>.toTransportErrors(): List<Error>? = this
    .map { it.toTransportRule() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun AppError.toTransportRule() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)

private fun AppState.toResult(): ResponseResult? = when (this) {
    AppState.RUNNING -> ResponseResult.SUCCESS
    AppState.FAILING -> ResponseResult.ERROR
    AppState.FINISHING -> ResponseResult.SUCCESS
    AppState.NONE -> null
}

internal fun AppGrade.toTransportRule(): Grade? =
    when (this) {
        AppGrade.A1 -> Grade.A1
        AppGrade.A2 -> Grade.A2
        AppGrade.B1 -> Grade.B1
        AppGrade.B2 -> Grade.B2
        AppGrade.C1 -> Grade.C1
        AppGrade.C2 -> Grade.C2
        AppGrade.NONE -> null
    }