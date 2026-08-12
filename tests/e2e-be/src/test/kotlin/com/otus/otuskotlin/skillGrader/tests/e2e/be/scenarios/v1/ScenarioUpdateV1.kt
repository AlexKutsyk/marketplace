package com.otus.otuskotlin.skillGrader.tests.e2e.be.scenarios.v1

import com.otus.otuskotlin.skillGrader.project.api.v1.models.ResponseResult
import com.otus.otuskotlin.skillGrader.project.api.v1.models.RuleCreateRequest
import com.otus.otuskotlin.skillGrader.project.api.v1.models.RuleCreateResponse
import com.otus.otuskotlin.skillGrader.project.api.v1.models.RuleDebug
import com.otus.otuskotlin.skillGrader.project.api.v1.models.RuleDeleteObject
import com.otus.otuskotlin.skillGrader.project.api.v1.models.RuleDeleteRequest
import com.otus.otuskotlin.skillGrader.project.api.v1.models.RuleDeleteResponse
import com.otus.otuskotlin.skillGrader.project.api.v1.models.RuleResponseObject
import com.otus.otuskotlin.skillGrader.project.api.v1.models.RuleUpdateObject
import com.otus.otuskotlin.skillGrader.project.api.v1.models.RuleUpdateRequest
import com.otus.otuskotlin.skillGrader.project.api.v1.models.RuleUpdateResponse
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import com.otus.otuskotlin.skillGrader.tests.e2e.be.base.client.Client
import com.otus.otuskotlin.skillGrader.tests.e2e.be.scenarios.v1.base.sendAndReceive
import com.otus.otuskotlin.skillGrader.tests.e2e.be.scenarios.v1.base.someCreateRule
import kotlin.test.assertEquals
import kotlin.test.fail

abstract class ScenarioUpdateV1(
    private val client: Client,
    private val debug: RuleDebug? = null
) {
    @Test
    fun update() = runBlocking {
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

        val uObj = RuleUpdateObject(
            id = cObj.id,
            lock = cObj.lock,
            name = "New name",
            grade = cObj.grade,
            minGrammarPercent = cObj.minGrammarPercent,
            minLexiconsPercent = cObj.minLexiconsPercent,
            minListeningPercent = cObj.minListeningPercent,
            timeWindowDays = cObj.timeWindowDays,
            priority = cObj.priority,
        )

        val resUpdate = client.sendAndReceive(
            "rule/update",
            RuleUpdateRequest(
                requestType = "update",
                debug = debug,
                requestRule = uObj,
            )
        ) as RuleUpdateResponse

        assertEquals(ResponseResult.SUCCESS, resUpdate.result)

        val ruObj: RuleResponseObject = resUpdate.rule ?: fail("No rule in Update response")
        assertEquals(uObj.name, ruObj.name)
        assertEquals(uObj.grade, ruObj.grade)
        assertEquals(uObj.minGrammarPercent, ruObj.minGrammarPercent)
        assertEquals(uObj.minLexiconsPercent, ruObj.minLexiconsPercent)
        assertEquals(uObj.minListeningPercent, ruObj.minListeningPercent)
        assertEquals(uObj.timeWindowDays, ruObj.timeWindowDays)
        assertEquals(uObj.priority, ruObj.priority)

        val resDelete = client.sendAndReceive(
            "rule/delete", RuleDeleteRequest(
                requestType = "delete",
                debug = debug,
                requestRule = RuleDeleteObject(cObj.id, resUpdate.rule?.lock),
            )
        ) as RuleDeleteResponse

        assertEquals(ResponseResult.SUCCESS, resDelete.result)
    }
}