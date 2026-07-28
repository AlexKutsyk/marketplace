package com.otus.otuskotlin.skillGrader.tests.e2e.be.docker

import com.otus.otuskotlin.skillGrader.tests.e2e.be.base.AbstractDockerCompose

object KtorDockerCompose : AbstractDockerCompose(
    "app-ktor_1", 8080, "docker-compose-ktor.yml"
)