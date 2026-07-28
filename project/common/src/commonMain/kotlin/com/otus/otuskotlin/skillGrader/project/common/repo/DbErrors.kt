package com.otus.otuskotlin.skillGrader.project.common.repo

import com.otus.otuskotlin.skillGrader.project.common.helpers.errorSystem
import com.otus.otuskotlin.skillGrader.project.common.models.AppError
import com.otus.otuskotlin.skillGrader.project.common.models.AppRule
import com.otus.otuskotlin.skillGrader.project.common.models.AppRuleId
import com.otus.otuskotlin.skillGrader.project.common.models.AppRuleLock
import com.otus.otuskotlin.skillGrader.project.common.repo.exceptions.RepoConcurrencyException
import com.otus.otuskotlin.skillGrader.project.common.repo.exceptions.RepoException

const val ERROR_GROUP_REPO = "repo"

fun errorNotFound(id: AppRuleId) = IDbRuleResponse.DbRuleErrorResponse(
    AppError(
        code = "$ERROR_GROUP_REPO-not-found",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Object with ID: ${id.asString()} is not Found",
    )
)

val errorEmptyId = IDbRuleResponse.DbRuleErrorResponse(
    AppError(
        code = "$ERROR_GROUP_REPO-empty-id",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Id must not be null or blank"
    )
)

fun errorRepoConcurrency(
    oldAd: AppRule,
    expectedLock: AppRuleLock,
    exception: Exception = RepoConcurrencyException(
        id = oldAd.id,
        expectedLock = expectedLock,
        actualLock = oldAd.lock,
    ),
) = IDbRuleResponse.DbRuleErrorWithDataResponse(
    rule = oldAd,
    error = AppError(
        code = "$ERROR_GROUP_REPO-concurrency",
        group = ERROR_GROUP_REPO,
        field = "lock",
        message = "The object with ID ${oldAd.id.asString()} has been changed concurrently by another user or process",
        exception = exception,
    )
)

fun errorEmptyLock(id: AppRuleId) = IDbRuleResponse.DbRuleErrorResponse(
    AppError(
        code = "$ERROR_GROUP_REPO-lock-empty",
        group = ERROR_GROUP_REPO,
        field = "lock",
        message = "Lock for Ad ${id.asString()} is empty that is not admitted"
    )
)

fun errorDb(e: RepoException) = IDbRuleResponse.DbRuleErrorResponse(
    errorSystem(
        violationCode = "dbLockEmpty",
        e = e
    )
)