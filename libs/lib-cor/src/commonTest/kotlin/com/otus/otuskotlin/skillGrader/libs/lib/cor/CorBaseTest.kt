package com.otus.otuskotlin.skillGrader.libs.lib.cor

import chain
import kotlinx.coroutines.test.runTest
import rootChain
import worker
import kotlin.test.Test
import kotlin.test.assertEquals

class CorBaseTest {

    @Test
    fun testCorSuccess() = runTest {
        val testContext = TestContext(condition = true)
        chain.exec(testContext)
        assertEquals("SUCCESS", testContext.result)
    }

    @Test
    fun testCorFailure() = runTest {
        val testContext = TestContext(condition = false)
        chain.exec(testContext)
        assertEquals("FAILURE ALL", testContext.result)
    }

    companion object {
        val chain = rootChain<TestContext> {
            worker {
                title = "Worker_1"
                description = "Description of Worker 1"
                on { condition == true }
                handle { result = "SUCCESS" }
            }

            chain {
                title = "Chain_1"
                description = "Description of Chain 1"
                on { condition == false }

                worker {
                    title = "Worker_2"
                    description = "Description of Worker 2"
                    on { condition == false }
                    handle { result = "FAILURE" }
                }

                worker {
                    title = "Worker_3"
                    description = "Description of Worker 3"
                    on { condition == false }
                    handle { result += " ALL" }
                }
            }
        }.build()


    }
}

data class TestContext(
    var condition: Boolean = false,
    var result: String = "",
)


/*
class ChainBuilder<T> {
    val workers = mutableListOf<WorkerBuilder<T>>()

    fun worker(function: WorkerBuilder<T>.() -> Unit) {
        workers.add(
            WorkerBuilder().apply { function() },
        )
    }

    fun chain(function: WorkerBuilder<T>.() -> Unit) {
        workers.add(
            WorkerBuilder().apply { function() },
        )
    }
}*/
