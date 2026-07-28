package com.otus.otuskotlin.skillGrader.tests.e2e.be.docker

import com.otus.otuskotlin.skillGrader.tests.e2e.be.base.AbstractDockerCompose

object KtorLinuxPGDockerCompose : AbstractDockerCompose(
    "app-ktor",
    8080,
    "docker-compose-ktor-pg-linux.yml",
)