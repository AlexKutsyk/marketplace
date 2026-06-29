class CorChain<T>(
    private val execs: List<ICorExec<T>>,
    title: String,
    description: String,
    blockOn: suspend T.() -> Boolean,
    blockException: suspend T.(e: Throwable) -> Unit,
) : AbstractCorExec<T>(
    title,
    description,
    blockOn,
    blockException
) {

    override suspend fun handle(context: T) {
        execs.forEach { exec ->
            exec.exec(context)
        }
    }
}

class CorChainBuilder<T> : CorExecBuilder<T>(), ICorChainBuilder<T> {
    private val workers = mutableListOf<ICorBuilder<T>>()

    override fun add(worker: ICorBuilder<T>) {
        workers.add(worker)
    }

    override fun build(): ICorExec<T> =
        CorChain(
            title = title,
            description = description,
            blockOn = blockOn,
            blockException = blockException,
            execs = workers.map { it.build() }
        )
}