import com.otus.otuskotlin.skillGrader.api.v1.models.*
import com.otus.otuskotlin.skillGrader.project.common.AppContext
import com.otus.otuskotlin.skillGrader.project.common.models.*
import com.otus.otuskotlin.skillGrader.project.common.stubs.AppStubs
import com.otus.otuskotlin.skillGrader.project.mappers.v1.fromTransport
import com.otus.otuskotlin.skillGrader.project.mappers.v1.toTransportCreateRule
import com.otus.otuskotlin.skillGrader.project.mappers.v1.toTransportRule
import com.otus.otuskotlin.skillGrader.project.stubs.AppRuleStub
import org.junit.Test
import kotlin.test.assertEquals

class MapperTest {
    @Test
    fun fromTransport() {
        val request = RuleCreateRequest(
            debug = RuleDebug(
                mode = RuleRequestDebugMode.STUB,
                stub = RuleRequestDebugStubs.SUCCESS,
            ),
            requestRule = AppRuleStub.get().toTransportCreateRule()
        )
        val expected = AppRuleStub.prepareResult {
            id = AppRuleId.NONE
            lock = AppRuleLock.NONE
            permissionsClient.clear()
        }

        val context = AppContext()
        context.fromTransport(request)

        assertEquals(AppStubs.SUCCESS, context.stubCase)
        assertEquals(AppWorkMode.STUB, context.workMode)
        assertEquals(expected, context.ruleRequest)
    }

    @Test
    fun toTransport() {
        val context = AppContext(
            requestId = AppRequestId("000"),
            command = AppCommand.CREATE,
            ruleResponse = AppRuleStub.get(),
            errors = mutableListOf(
                AppError(
                    code = "error",
                    group = "request",
                    field = "name",
                    message = "wrong name",
                )
            ),
            state = AppState.RUNNING,
        )

        val request = context.toTransportRule() as RuleCreateResponse

        assertEquals(request.rule, AppRuleStub.get().toTransportRule())
        assertEquals(1, request.errors?.size)
        assertEquals("error", request.errors?.firstOrNull()?.code)
        assertEquals("request", request.errors?.firstOrNull()?.group)
        assertEquals("name", request.errors?.firstOrNull()?.field)
        assertEquals("wrong name", request.errors?.firstOrNull()?.message)
    }
}