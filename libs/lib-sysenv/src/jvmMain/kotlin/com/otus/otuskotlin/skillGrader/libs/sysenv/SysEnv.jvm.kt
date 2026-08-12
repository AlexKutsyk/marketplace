package com.otus.otuskotlin.skillGrader.libs.sysenv

actual fun sysEnv(key: String): String? = System.getenv(key)