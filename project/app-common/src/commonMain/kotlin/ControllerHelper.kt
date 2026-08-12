import com.otus.otuskotlin.skillGrader.project.app.common.IServiceSettings
import com.otus.otuskotlin.skillGrader.project.common.AppContext
import com.otus.otuskotlin.skillGrader.project.common.helpers.asAppError
import com.otus.otuskotlin.skillGrader.project.common.models.AppCommand
import com.otus.otuskotlin.skillGrader.project.common.models.AppState
import com.otus.otuskotlin.skillGrader.project.log.api.v1.toLog
import kotlin.reflect.KClass
import kotlin.time.Clock

suspend inline fun <T> IServiceSettings.controllerHelper(
    crossinline getRequest: suspend AppContext.() -> Unit,
    crossinline toResponse: suspend AppContext.() -> T,
    clazz: KClass<*>,
    logId: String,
): T {
    val logger = corSettings.loggerProvider.logger(clazz)
    val ctx = AppContext(
        timeStart = Clock.System.now(),
    )
    return try {
        ctx.getRequest()
        logger.info(
            msg = "Request $logId started for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        processor.execute(ctx)
        logger.info(
            msg = "Request $logId processed for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        ctx.toResponse()
    } catch (e: Throwable) {
        logger.error(
            msg = "Request $logId failed for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        ctx.state = AppState.FAILING
        ctx.errors.add(e.asAppError())
        processor.execute(ctx)
        if (ctx.command == AppCommand.NONE) {
            ctx.command = AppCommand.READ
        }
        ctx.toResponse()
    }
}
