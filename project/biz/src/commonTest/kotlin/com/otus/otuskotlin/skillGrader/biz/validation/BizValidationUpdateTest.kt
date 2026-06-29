package com.otus.otuskotlin.skillGrader.biz.validation

import com.otus.otuskotlin.skillGrader.project.common.models.AppCommand
import kotlin.test.Test

class BizValidationUpdateTest: BaseBizValidationTest() {
    override val command = AppCommand.UPDATE

    @Test fun correctName() = validationNameCorrect(command, processor)
    @Test fun trimName() = validationNameTrim(command, processor)
    @Test fun emptyName() = validationNameEmpty(command, processor)
    @Test fun badSymbolsName() = validationNameSymbols(command, processor)

    @Test fun correctMinGrammarPercent() = validationMinGrammarPercentCorrect(command, processor)
    @Test fun negativeMinGrammarPercent() = validationMinGrammarPercentNegative(command, processor)
    @Test fun outOfRangeMinGrammarPercent() = validationMinGrammarPercentOutOfRange(command, processor)

    @Test fun correctMinLexiconsPercent() = validationMinLexiconsPercentCorrect(command, processor)
    @Test fun negativeMinLexiconsPercent() = validationMinLexiconsPercentNegative(command, processor)
    @Test fun outOfRangeMinLexiconsPercent() = validationMinLexiconsPercentOutOfRange(command, processor)

    @Test fun correctMinListeningPercent() = validationMinListeningPercentCorrect(command, processor)
    @Test fun negativeMinListeningPercent() = validationMinListeningPercentNegative(command, processor)
    @Test fun outOfRangeMinListeningPercent() = validationMinListeningPercentOutOfRange(command, processor)

    @Test fun correctTimeWindowDays() = validationTimeWindowDaysCorrect(command, processor)
    @Test fun negativeTimeWindowDays() = validationTimeWindowDaysNegative(command, processor)
    @Test fun outOfRangeTimeWindowDays() = validationTimeWindowDaysOutOfRange(command, processor)

    @Test fun correctPriority() = validationPriorityCorrect(command, processor)
    @Test fun negativePriority() = validationPriorityNegative(command, processor)
    @Test fun outOfRangePriority() = validationPriorityOutOfRange(command, processor)

    @Test fun correctId() = validationIdCorrect(command, processor)
    @Test fun trimId() = validationIdTrim(command, processor)
    @Test fun emptyId() = validationIdEmpty(command, processor)
    @Test fun badFormatId() = validationIdFormat(command, processor)

    @Test fun correctLock() = validationLockCorrect(command, processor)
    @Test fun trimLock() = validationLockTrim(command, processor)
    @Test fun emptyLock() = validationLockEmpty(command, processor)
    @Test fun badFormatLock() = validationLockFormat(command, processor)
}