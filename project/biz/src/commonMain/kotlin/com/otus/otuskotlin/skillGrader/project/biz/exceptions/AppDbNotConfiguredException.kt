package com.otus.otuskotlin.skillGrader.project.biz.exceptions

import com.otus.otuskotlin.skillGrader.project.common.models.AppWorkMode

class AppDbNotConfiguredException(val workMode: AppWorkMode): Exception(
    "Database is not configured properly for workMode $workMode"
)