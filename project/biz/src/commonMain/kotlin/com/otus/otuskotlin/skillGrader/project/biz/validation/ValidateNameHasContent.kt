package com.otus.otuskotlin.skillGrader.project.biz.validation

import ICorChainBuilder
import com.otus.otuskotlin.skillGrader.project.common.AppContext
import com.otus.otuskotlin.skillGrader.project.common.helpers.errorValidation
import com.otus.otuskotlin.skillGrader.project.common.helpers.fail
import worker

fun ICorChainBuilder<AppContext>.validateNameHasContent(title: String) = worker {
    this.title = title
    this.description = """
        Проверяем, что у нас есть какие-то слова в названии.
        Отказываем в создании правила, если в названии содержатся только бессмысленные символы типа %^&^$^%#^))&^*&%^^&
    """.trimIndent()
    val regExp = Regex("\\p{L}")
    on { ruleValidating.name.isNotEmpty() && !ruleValidating.name.contains(regExp) }
    handle {
        fail(
            errorValidation(
            field = "name",
            violationCode = "noContent",
            description = "field must contain letters"
        )
        )
    }
}