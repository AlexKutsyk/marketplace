package com.otus.otuskotlin.skillGrader.biz.validation

import com.otus.otuskotlin.skillGrader.project.common.models.AppCommand
import kotlin.test.Test

class BizValidationReadTest: BaseBizValidationTest() {
    override val command = AppCommand.READ

    @Test fun correctId() = validationIdCorrect(command, processor)
    @Test fun trimId() = validationIdTrim(command, processor)
    @Test fun emptyId() = validationIdEmpty(command, processor)
    @Test fun badFormatId() = validationIdFormat(command, processor)
}