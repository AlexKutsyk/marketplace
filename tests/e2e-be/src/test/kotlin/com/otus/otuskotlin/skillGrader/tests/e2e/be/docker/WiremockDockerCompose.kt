package com.otus.otuskotlin.skillGrader.tests.e2e.be.docker

import com.otus.otuskotlin.skillGrader.tests.e2e.be.base.AbstractDockerCompose

object WiremockDockerCompose : AbstractDockerCompose(
    "app-wiremock", 8080, "docker-compose-wiremock.yml"
)