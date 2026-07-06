package com.otus.otuskotlin.skillGrader.biz.validation

import com.otus.otuskotlin.skillGrader.project.biz.RuleProcessor
import com.otus.otuskotlin.skillGrader.project.common.AppCorSettings
import com.otus.otuskotlin.skillGrader.project.common.models.AppCommand

abstract class BaseBizValidationTest {
    protected abstract val command: AppCommand
    private val settings by lazy { AppCorSettings() }
    protected val processor by lazy { RuleProcessor(settings) }
}