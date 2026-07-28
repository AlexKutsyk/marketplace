package com.otus.otuskotlin.skillGrader.project.jackson

import com.otus.otuskotlin.skillGrader.project.api.v1.models.Grade
import com.otus.otuskotlin.skillGrader.project.api.v1.models.IRequest
import com.otus.otuskotlin.skillGrader.project.api.v1.models.RuleCreateObject
import com.otus.otuskotlin.skillGrader.project.api.v1.models.RuleCreateRequest
import com.otus.otuskotlin.skillGrader.project.api.v1.models.RuleDebug
import com.otus.otuskotlin.skillGrader.project.api.v1.models.RuleRequestDebugMode
import com.otus.otuskotlin.skillGrader.project.api.v1.models.RuleRequestDebugStubs
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNull

class RequestV1SerializationTest {
    private val request = RuleCreateRequest(
        requestType = "create",
        debug = RuleDebug(
            mode = RuleRequestDebugMode.STUB,
            stub = RuleRequestDebugStubs.BAD_NAME
        ),
        requestRule = RuleCreateObject(
            name = "test rule",
            grade = Grade.A1,
            minGrammarPercent = 30,
            minLexiconsPercent = 30,
            minListeningPercent = 30,
            timeWindowDays = 0,
            priority = 1
        )
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(request)

        assertContains(json, Regex("\"name\":\\s*\"test rule\""))
        assertContains(json, Regex("\"grade\":\\s*\"A1\""))
        assertContains(json, Regex("\"minGrammarPercent\":\\s*30"))
        assertContains(json, Regex("\"minLexiconsPercent\":\\s*30"))
        assertContains(json, Regex("\"minListeningPercent\":\\s*30"))
        assertContains(json, Regex("\"timeWindowDays\":\\s*0"))
        assertContains(json, Regex("\"priority\":\\s*1"))
        assertContains(json, Regex("\"stub\":\\s*\"badName\""))
        assertContains(json, Regex("\"requestType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(request)
        println(json)
        val obj = apiV1Mapper.readValue(json, IRequest::class.java) as RuleCreateRequest

        assertEquals(request, obj)
    }

    @Test
    fun deserializeNaked() {
        val jsonString = """
            {"requestRule": null}
        """.trimIndent()
        val obj = apiV1Mapper.readValue(jsonString, RuleCreateRequest::class.java)

        assertNull(obj.requestRule)
    }
}
