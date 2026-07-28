package com.otus.otuskotlin.skillGrader.project.jackson

import com.otus.otuskotlin.skillGrader.project.api.v1.models.Grade
import com.otus.otuskotlin.skillGrader.project.api.v1.models.IResponse
import com.otus.otuskotlin.skillGrader.project.api.v1.models.RuleCreateResponse
import com.otus.otuskotlin.skillGrader.project.api.v1.models.RulePermissions
import com.otus.otuskotlin.skillGrader.project.api.v1.models.RuleResponseObject
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseV1SerializationTest {
    private val response = RuleCreateResponse(
        rule = RuleResponseObject(
            name = "test rule",
            grade = Grade.A1,
            minGrammarPercent = 30,
            minLexiconsPercent = 30,
            minListeningPercent = 30,
            timeWindowDays = 0,
            priority = 1,
            id = "001",
            permissions = setOf(RulePermissions.READ, RulePermissions.DELETE, RulePermissions.UPDATE)
        ),
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(response)

        assertContains(json, Regex("\"name\":\\s*\"test rule\""))
        assertContains(json, Regex("\"grade\":\\s*\"A1\""))
        assertContains(json, Regex("\"minGrammarPercent\":\\s*30"))
        assertContains(json, Regex("\"minLexiconsPercent\":\\s*30"))
        assertContains(json, Regex("\"minListeningPercent\":\\s*30"))
        assertContains(json, Regex("\"timeWindowDays\":\\s*0"))
        assertContains(json, Regex("\"priority\":\\s*1"))
        assertContains(json, Regex("\"id\":\\s*\"001\""))
        assertContains(json, Regex("\"responseType\":\\s*\"create\""))
        assertContains(json, Regex("\"permissions\":\\s*\\[\"read\",\"delete\",\"update\"]"))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(response)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java) as RuleCreateResponse

        assertEquals(response, obj)
    }
}