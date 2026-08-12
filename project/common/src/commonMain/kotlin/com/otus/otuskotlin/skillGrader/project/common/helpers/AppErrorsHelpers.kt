package com.otus.otuskotlin.skillGrader.project.common.helpers

import com.otus.otuskotlin.skillGrader.logging.common.LogLevel
import com.otus.otuskotlin.skillGrader.project.common.AppContext
import com.otus.otuskotlin.skillGrader.project.common.models.AppError
import com.otus.otuskotlin.skillGrader.project.common.models.AppState

fun Throwable.asAppError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = AppError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)

fun AppContext.fail(error: AppError) {
    addError(error)
    state = AppState.FAILING
}

fun AppContext.fail(errors: Collection<AppError>) {
    addErrors(errors)
    state = AppState.FAILING
}

private fun AppContext.addError(error: AppError) = errors.add(error)
private fun AppContext.addErrors(error: Collection<AppError>) = errors.addAll(error)

fun errorValidation(
    field: String,
    /**
     * Код, характеризующий ошибку. Не должен включать имя поля или указание на валидацию.
     * Например: empty, badSymbols, tooLong, etc
     */
    violationCode: String,
    description: String,
    level: LogLevel = LogLevel.ERROR,
) = AppError(
    code = "validation-$field-$violationCode",
    field = field,
    group = "validation",
    message = "Validation error for field $field: $description",
    level = level,
)

inline fun errorSystem(
    violationCode: String,
    level: LogLevel = LogLevel.ERROR,
    e: Throwable,
) = AppError(
    code = "system-$violationCode",
    group = "system",
    message = "System error occurred. Our stuff has been informed, please retry later",
    level = level,
    exception = e,
)