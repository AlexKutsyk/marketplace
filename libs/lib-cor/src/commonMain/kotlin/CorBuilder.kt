interface ICorBuilder<T> {
    var title: String
    var description: String
    fun on(function: suspend T.() -> Boolean)
    fun except(function: suspend T.(e: Throwable) -> Unit)
    fun build(): ICorExec<T>
}

interface ICorChainBuilder<T> : ICorBuilder<T> {
    fun add(worker: ICorBuilder<T>)
}

interface ICorWorkerBuilder<T> : ICorBuilder<T> {
    fun handle(function: suspend T.() -> Unit)
}

fun <T> rootChain(function: ICorChainBuilder<T>.() -> Unit): ICorChainBuilder<T> =
    CorChainBuilder<T>().apply { function() }

fun <T> ICorChainBuilder<T>.chain(function: ICorChainBuilder<T>.() -> Unit) {
    add(CorChainBuilder<T>().apply(function))
}

fun <T> ICorChainBuilder<T>.worker(function: ICorWorkerBuilder<T>.() -> Unit) {
    add(CorWorkerBuilder<T>().apply(function))
}

fun <T> ICorChainBuilder<T>.worker(
    title: String,
    description: String = "",
    blockHandle: T.() -> Unit,
) {
    add(
        CorWorkerBuilder<T>().apply{
            this.title = title
            this.description = description
            this.handle(function = blockHandle)
        }
    )
}