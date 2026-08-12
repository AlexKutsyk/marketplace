package com.otus.otuskotlin.skillGrader.project.ktor.repo

import com.otus.otuskotlin.skillGrader.api.v1.models.IRequest
import com.otus.otuskotlin.skillGrader.api.v1.models.IResponse
import com.otus.otuskotlin.skillGrader.api.v1.models.RuleCreateRequest
import com.otus.otuskotlin.skillGrader.api.v1.models.RuleCreateResponse
import com.otus.otuskotlin.skillGrader.api.v1.models.RuleDebug
import com.otus.otuskotlin.skillGrader.api.v1.models.RuleDeleteRequest
import com.otus.otuskotlin.skillGrader.api.v1.models.RuleDeleteResponse
import com.otus.otuskotlin.skillGrader.api.v1.models.RuleReadRequest
import com.otus.otuskotlin.skillGrader.api.v1.models.RuleReadResponse
import com.otus.otuskotlin.skillGrader.api.v1.models.RuleRequestDebugMode
import com.otus.otuskotlin.skillGrader.api.v1.models.RuleSearchFilter
import com.otus.otuskotlin.skillGrader.api.v1.models.RuleSearchRequest
import com.otus.otuskotlin.skillGrader.api.v1.models.RuleSearchResponse
import com.otus.otuskotlin.skillGrader.api.v1.models.RuleUpdateRequest
import com.otus.otuskotlin.skillGrader.api.v1.models.RuleUpdateResponse
import com.otus.otuskotlin.skillGrader.project.common.models.AppRuleId
import com.otus.otuskotlin.skillGrader.project.common.models.AppRuleLock
import com.otus.otuskotlin.skillGrader.project.ktor.ServiceSettings
import com.otus.otuskotlin.skillGrader.project.ktor.moduleJvm
import com.otus.otuskotlin.skillGrader.project.mappers.v1.toTransportCreateRule
import com.otus.otuskotlin.skillGrader.project.mappers.v1.toTransportDeleteRule
import com.otus.otuskotlin.skillGrader.project.mappers.v1.toTransportReadRule
import com.otus.otuskotlin.skillGrader.project.mappers.v1.toTransportUpdateRule
import com.otus.otuskotlin.skillGrader.project.stubs.AppRuleStub
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import junit.runner.Version.id
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

abstract class RuleRepoBaseTest {
    abstract val workMode: RuleRequestDebugMode
    abstract val appSettingsCreate: ServiceSettings
    abstract val appSettingsRead: ServiceSettings
    abstract val appSettingsUpdate: ServiceSettings
    abstract val appSettingsDelete: ServiceSettings
    abstract val appSettingsSearch: ServiceSettings

    protected val uuidOld = "10000000-0000-0000-0000-000000000001"
    protected val uuidNew = "10000000-0000-0000-0000-000000000002"

    //    protected val uuidSup = "10000000-0000-0000-0000-000000000003"
    protected val initRule = AppRuleStub.prepareResult {
        id = AppRuleId(uuidOld)
//        ruleType = MkplDealSide.DEMAND
        lock = AppRuleLock(uuidOld)
    }
//    protected val initARuleSupply = AppRuleStub.prepareResult {
//        id = AppRuleId(uuidSup)
////        adType = MkplDealSide.SUPPLY
//    }

    @Test
    fun create() {
        val rule = initRule.toTransportCreateRule()
        testApplication(
            config = appSettingsCreate,
            functionString = "create",
            request = RuleCreateRequest(
                requestRule = rule,
                debug = RuleDebug(mode = workMode),
            ),
        ) { response ->
            val responseObj = response.body<RuleCreateResponse>()
            assertEquals(200, response.status.value)
            assertEquals(uuidNew, responseObj.rule?.id)
            assertEquals(rule.name, responseObj.rule?.name)
            assertEquals(rule.grade, responseObj.rule?.grade)
            assertEquals(rule.minGrammarPercent, responseObj.rule?.minGrammarPercent)
            assertEquals(rule.minLexiconsPercent, responseObj.rule?.minLexiconsPercent)
            assertEquals(rule.minListeningPercent, responseObj.rule?.minListeningPercent)
            assertEquals(rule.timeWindowDays, responseObj.rule?.timeWindowDays)
            assertEquals(rule.priority, responseObj.rule?.priority)
            assertEquals(uuidNew, responseObj.rule?.lock)
        }
    }

    @Test
    fun read() {
        val rule = initRule.toTransportReadRule()
        testApplication(
            config = appSettingsRead,
            functionString = "read",
            request = RuleReadRequest(
                requestRule = rule,
                debug = RuleDebug(mode = workMode),
            ),
        ) { response ->
            val responseObj = response.body<IResponse>() as RuleReadResponse
            assertEquals(200, response.status.value)
            assertEquals(uuidOld, responseObj.rule?.id)
        }
    }

    @Test
    fun update() {
        val rule = initRule.toTransportUpdateRule()
        testApplication(
            config = appSettingsUpdate,
            functionString = "update",
            request = RuleUpdateRequest(
                requestRule = rule,
                debug = RuleDebug(mode = workMode),
            ),
        ) { response ->
            val responseObj = response.body<RuleUpdateResponse>()
            assertEquals(200, response.status.value)
            assertEquals(rule.id, responseObj.rule?.id)
            assertEquals(rule.name, responseObj.rule?.name)
            assertEquals(rule.grade, responseObj.rule?.grade)
            assertEquals(rule.minGrammarPercent, responseObj.rule?.minGrammarPercent)
            assertEquals(rule.minLexiconsPercent, responseObj.rule?.minLexiconsPercent)
            assertEquals(rule.minListeningPercent, responseObj.rule?.minListeningPercent)
            assertEquals(rule.timeWindowDays, responseObj.rule?.timeWindowDays)
            assertEquals(rule.priority, responseObj.rule?.priority)
            assertEquals(uuidNew, responseObj.rule?.lock)
        }
    }

    @Test
    fun delete() {
        val rule = initRule.toTransportDeleteRule()
        testApplication(
            config = appSettingsDelete,
            functionString = "delete",
            request = RuleDeleteRequest(
                requestRule = rule,
                debug = RuleDebug(mode = workMode),
            ),
        ) { response ->
            val responseObj = response.body<RuleDeleteResponse>()
            assertEquals(200, response.status.value)
            assertEquals(uuidOld, responseObj.rule?.id)
        }
    }

    @Test
    fun search() = testApplication(
        config = appSettingsSearch,
        functionString = "search",
        request = RuleSearchRequest(
            ruleFilter = RuleSearchFilter(),
            debug = RuleDebug(mode = workMode),
        ),
    ) { response ->
        val responseObj = response.body<RuleSearchResponse>()
        assertEquals(200, response.status.value)
        assertNotEquals(0, responseObj.rules?.size)
        assertEquals(uuidOld, responseObj.rules?.first()?.id)
    }

    private inline fun <reified T : IRequest> testApplication(
        config: ServiceSettings,
        functionString: String,
        request: T,
        crossinline function: suspend (HttpResponse) -> Unit,
    ): Unit = testApplication {
        application { moduleJvm(serviceSettings = config) }
        val client = createClient {
            install(ContentNegotiation) {
                jackson()
            }
        }
        val response = client.post("/v1/rule/$functionString") {
            contentType(ContentType.Application.Json)
            header("X-Trace-Id", "12345")
            setBody(request)
        }
        function(response)
    }
}