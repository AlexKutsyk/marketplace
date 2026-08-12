package com.otus.otuskotlin.skillGrader.project.ktor.v1

import com.otus.otuskotlin.skillGrader.api.v1.models.IRequest
import com.otus.otuskotlin.skillGrader.api.v1.models.IResponse
import com.otus.otuskotlin.skillGrader.project.ktor.ServiceSettings
import com.otus.otuskotlin.skillGrader.project.mappers.v1.fromTransport
import com.otus.otuskotlin.skillGrader.project.mappers.v1.toTransportRule
import controllerHelper
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlin.reflect.KClass

suspend inline fun <reified Q : IRequest, @Suppress("unused") reified R : IResponse> ApplicationCall.processV1(
    appSettings: ServiceSettings,
    clazz: KClass<*>,
    logId: String,
) = appSettings.controllerHelper(
    getRequest = { fromTransport(receive<Q>()) },
    toResponse = { respond(toTransportRule()) },
    clazz = clazz,
    logId = logId,
)