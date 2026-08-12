package com.otus.otuskotlin.skillGrader.tests.e2e.be

import com.otus.otuskotlin.skillGrader.project.api.v1.models.RuleDebug
import com.otus.otuskotlin.skillGrader.project.api.v1.models.RuleRequestDebugMode
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import com.otus.otuskotlin.skillGrader.tests.e2e.be.base.BaseContainerTest
import com.otus.otuskotlin.skillGrader.tests.e2e.be.base.client.Client
import com.otus.otuskotlin.skillGrader.tests.e2e.be.base.client.RestClient
import com.otus.otuskotlin.skillGrader.tests.e2e.be.docker.KtorJvmPGDockerCompose
import com.otus.otuskotlin.skillGrader.tests.e2e.be.scenarios.v1.ScenariosV1

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Disabled("Не работает с авторизацией")
class TestPgJvm: BaseContainerTest(KtorJvmPGDockerCompose) {
    private val client: Client = RestClient(compose)
    @Test
    fun info() {
        println("${this::class.simpleName}")
    }

    @Nested
    internal inner class V1: ScenariosV1(client, RuleDebug(mode = RuleRequestDebugMode.PROD))
}