package com.otus.otuskotlin.skillGrader.tests.e2e.be.scenarios.v1

import com.otus.otuskotlin.skillGrader.project.api.v1.models.ResponseResult
import com.otus.otuskotlin.skillGrader.project.api.v1.models.RuleCreateRequest
import com.otus.otuskotlin.skillGrader.project.api.v1.models.RuleCreateResponse
import com.otus.otuskotlin.skillGrader.project.api.v1.models.RuleDebug
import com.otus.otuskotlin.skillGrader.project.api.v1.models.RuleDeleteObject
import com.otus.otuskotlin.skillGrader.project.api.v1.models.RuleDeleteRequest
import com.otus.otuskotlin.skillGrader.project.api.v1.models.RuleDeleteResponse
import com.otus.otuskotlin.skillGrader.project.api.v1.models.RuleResponseObject
import com.otus.otuskotlin.skillGrader.project.api.v1.models.RuleSearchFilter
import com.otus.otuskotlin.skillGrader.project.api.v1.models.RuleSearchRequest
import com.otus.otuskotlin.skillGrader.project.api.v1.models.RuleSearchResponse
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import com.otus.otuskotlin.skillGrader.tests.e2e.be.base.client.Client
import com.otus.otuskotlin.skillGrader.tests.e2e.be.scenarios.v1.base.sendAndReceive
import com.otus.otuskotlin.skillGrader.tests.e2e.be.scenarios.v1.base.someCreateRule
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.fail

abstract class ScenarioSearchV1(
    private val client: Client,
    private val debug: RuleDebug? = null
) {
    @Test
    fun search() = runBlocking {
        val objs = listOf(
            someCreateRule,
            someCreateRule.copy(name = "Easy A1"),
            someCreateRule.copy(name = "Strong A1"),
        ).map { obj ->
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
            cObj
        }

        val sObj = RuleSearchFilter(searchString = "A1")
        val resSearch = client.sendAndReceive(
            "rule/search",
            RuleSearchRequest(
                requestType = "search",
                debug = debug,
                ruleFilter = sObj,
            )
        ) as RuleSearchResponse

        assertEquals(ResponseResult.SUCCESS, resSearch.result)

        val rsObj: List<RuleResponseObject> = resSearch.rules ?: fail("No rules in Search response")
        val titles = rsObj.map { it.name }
        assertContains(titles, "Easy A1")
        assertContains(titles, "Strong A1")

        objs.forEach { obj ->
            val resDelete = client.sendAndReceive(
                "rule/delete", RuleDeleteRequest(
                    requestType = "delete",
                    debug = debug,
                    requestRule = RuleDeleteObject(obj.id, obj.lock),
                )
            ) as RuleDeleteResponse

            assertEquals(ResponseResult.SUCCESS, resDelete.result)
        }
    }
}