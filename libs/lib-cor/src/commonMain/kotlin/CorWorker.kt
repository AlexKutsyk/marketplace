class CorWorker<T>(
    title: String,
    description: String,
    blockOn: suspend T.() -> Boolean,
    blockException: suspend T.(e: Throwable) -> Unit,
    private val blockHandle: suspend T.() -> Unit = {},
) : AbstractCorExec<T>(
    title,
    description,
    blockOn,
    blockException
) {
    override suspend fun handle(context: T) {
        blockHandle(context)
    }
}

class CorWorkerBuilder<T> : CorExecBuilder<T>(), ICorWorkerBuilder<T> {
    private var blockHandle: suspend T.() -> Unit = {}

    override fun handle(function: suspend T.() -> Unit) {
        blockHandle = function
    }

    override fun build(): ICorExec<T> =
        CorWorker(
            title = title,
            description = description,
            blockOn = blockOn,
            blockException = blockException,
            blockHandle = blockHandle,
        )
}