abstract class AbstractCorExec<T>(
    override val title: String,
    override val description: String,
    private val blockOn: suspend T.() -> Boolean = { false },
    private val blockException: suspend T.(e: Throwable) -> Unit = {}
) : ICorExec<T> {
    protected abstract suspend fun handle(context: T)

    override suspend fun exec(context: T) {
        if (blockOn(context)) {
            try {
                handle(context)
            } catch (e: Exception) {
                blockException(context, e)
            }
        }
    }
}

abstract class CorExecBuilder<T> : ICorBuilder<T> {
    protected var blockOn: suspend T.() -> Boolean = { true }
    protected var blockException: suspend T.(e: Throwable) -> Unit = {}

    override var title: String = ""
    override var description: String = ""

    override fun on(function: suspend T.() -> Boolean) { blockOn = function }
    override fun except(function: suspend T.(e: Throwable) -> Unit) { blockException = function }
}