package com.otus.otuskotlin.skillGrader.tests.e2e.be.scenarios.v1.base

import co.touchlab.kermit.Logger
import com.otus.otuskotlin.skillGrader.project.api.v1.models.IRequest
import com.otus.otuskotlin.skillGrader.project.api.v1.models.IResponse
import com.otus.otuskotlin.skillGrader.project.jackson.apiV1RequestSerialize
import com.otus.otuskotlin.skillGrader.project.jackson.apiV1ResponseDeserialize
import com.otus.otuskotlin.skillGrader.tests.e2e.be.base.client.Client

private val log = Logger

suspend fun Client.sendAndReceive(path: String, request: IRequest): IResponse {
    val requestBody = apiV1RequestSerialize(request)
    log.i { "Send to v1/$path\n$requestBody" }

    val responseBody = sendAndReceive("v1", path, requestBody)
    log.i { "Received\n$responseBody" }

    return apiV1ResponseDeserialize(responseBody)
}