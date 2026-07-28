package com.otus.otuskotlin.skillGrader.tests.e2e.be.scenarios.v1

import com.otus.otuskotlin.skillGrader.project.api.v1.models.ResponseResult
import com.otus.otuskotlin.skillGrader.project.api.v1.models.RuleCreateRequest
import com.otus.otuskotlin.skillGrader.project.api.v1.models.RuleCreateResponse
import com.otus.otuskotlin.skillGrader.project.api.v1.models.RuleDebug
import com.otus.otuskotlin.skillGrader.project.api.v1.models.RuleDeleteObject
import com.otus.otuskotlin.skillGrader.project.api.v1.models.RuleDeleteRequest
import com.otus.otuskotlin.skillGrader.project.api.v1.models.RuleDeleteResponse
import com.otus.otuskotlin.skillGrader.project.api.v1.models.RuleReadObject
import com.otus.otuskotlin.skillGrader.project.api.v1.models.RuleReadRequest
import com.otus.otuskotlin.skillGrader.project.api.v1.models.RuleReadResponse
import com.otus.otuskotlin.skillGrader.project.api.v1.models.RuleResponseObject
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import com.otus.otuskotlin.skillGrader.tests.e2e.be.base.client.Client
import com.otus.otuskotlin.skillGrader.tests.e2e.be.scenarios.v1.base.sendAndReceive
import com.otus.otuskotlin.skillGrader.tests.e2e.be.scenarios.v1.base.someCreateRule
import kotlin.test.assertEquals
import kotlin.test.fail

abstract class ScenarioReadV1(
    private val client: Client,
    private val debug: RuleDebug? = null
) {
    @Test
    fun read() = runBlocking {
        val obj = someCreateRule
        val resCreate = client.sendAndReceive(
            "rule/create", RuleCreateRequest(
                requestType = "create",
                debug = debug,
                requestRule = obj,
            )
        ) as RuleCreateResponse

        assertEquals(ResponseResult.SUCCESS, resCreate.result)

        val cObj: RuleResponseObject = resCreate.rule ?: fail("No rule in Create response")
        assertEquals(obj.name, cObj.name)
        assertEquals(obj.grade, cObj.grade)
        assertEquals(obj.minGrammarPercent, cObj.minGrammarPercent)
        assertEquals(obj.minLexiconsPercent, cObj.minLexiconsPercent)
        assertEquals(obj.minListeningPercent, cObj.minListeningPercent)
        assertEquals(obj.timeWindowDays, cObj.timeWindowDays)
        assertEquals(obj.priority, cObj.priority)

        val rObj = RuleReadObject(
            id = cObj.id,
        )
        val resRead = client.sendAndReceive(
            "rule/read",
            RuleReadRequest(
                requestType = "read",
                debug = debug,
                requestRule = rObj,
            )
        ) as RuleReadResponse

        assertEquals(ResponseResult.SUCCESS, resRead.result)

        val rrObj: RuleResponseObject = resRead.rule ?: fail("No rule in Read response")
        assertEquals(obj.name, rrObj.name)
        assertEquals(obj.grade, rrObj.grade)
        assertEquals(obj.minGrammarPercent, rrObj.minGrammarPercent)
        assertEquals(obj.minLexiconsPercent, rrObj.minLexiconsPercent)
        assertEquals(obj.minListeningPercent, rrObj.minListeningPercent)
        assertEquals(obj.timeWindowDays, rrObj.timeWindowDays)
        assertEquals(obj.priority, rrObj.priority)

        val resDelete = client.sendAndReceive(
            "rule/delete", RuleDeleteRequest(
                requestType = "delete",
                debug = debug,
                requestRule = RuleDeleteObject(cObj.id, cObj.lock),
            )
        ) as RuleDeleteResponse

        assertEquals(ResponseResult.SUCCESS, resDelete.result)

        val dObj: RuleResponseObject = resDelete.rule ?: fail("No rule in Delete response")
        assertEquals(obj.name, dObj.name)
        assertEquals(obj.grade, dObj.grade)
        assertEquals(obj.minGrammarPercent, dObj.minGrammarPercent)
        assertEquals(obj.minLexiconsPercent, dObj.minLexiconsPercent)
        assertEquals(obj.minListeningPercent, dObj.minListeningPercent)
        assertEquals(obj.timeWindowDays, dObj.timeWindowDays)
        assertEquals(obj.priority, dObj.priority)
    }
}