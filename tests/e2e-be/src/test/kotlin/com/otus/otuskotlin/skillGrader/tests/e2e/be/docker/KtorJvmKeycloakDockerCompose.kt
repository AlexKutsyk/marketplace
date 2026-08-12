package com.otus.otuskotlin.skillGrader.tests.e2e.be.docker

import com.otus.otuskotlin.skillGrader.tests.e2e.be.base.AbstractDockerCompose

object KtorJvmKeycloakDockerCompose : AbstractDockerCompose(
    "envoy", 8080, "docker-compose-ktor-keycloak-jvm.yml"
)