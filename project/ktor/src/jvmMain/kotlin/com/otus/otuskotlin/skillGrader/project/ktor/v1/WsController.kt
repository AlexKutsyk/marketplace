package com.otus.otuskotlin.skillGrader.project.ktor.v1

import com.fasterxml.jackson.module.kotlin.readValue
import com.otus.otuskotlin.skillGrader.api.v1.models.IRequest
import com.otus.otuskotlin.skillGrader.project.common.models.AppCommand
import com.otus.otuskotlin.skillGrader.project.jackson.apiV1Mapper
import com.otus.otuskotlin.skillGrader.project.ktor.ServiceSettings
import com.otus.otuskotlin.skillGrader.project.ktor.base.KtorWsSessionV1
import com.otus.otuskotlin.skillGrader.project.mappers.v1.fromTransport
import com.otus.otuskotlin.skillGrader.project.mappers.v1.toTransportInit
import com.otus.otuskotlin.skillGrader.project.mappers.v1.toTransportRule
import controllerHelper
import io.ktor.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow

import kotlin.reflect.KClass

private val classWsV1: KClass<*> = WebSocketSession::wsHandlerV1::class
suspend fun WebSocketSession.wsHandlerV1(appSettings: ServiceSettings) = with(KtorWsSessionV1(this)) {
    val sessions = appSettings.corSettings.wsSessions
    sessions.add(this)

    // Handle init request
    appSettings.controllerHelper(
        {
            command = AppCommand.INIT
            wsSession = this@with
        },
        { outgoing.send(Frame.Text(apiV1Mapper.writeValueAsString(toTransportInit()))) },
        classWsV1,
        "wsV1-init"
    )

    // Handle flow
    incoming.receiveAsFlow().map {
        val frame = it as? Frame.Text ?: return@map
        // Handle without flow destruction
        try {
            appSettings.controllerHelper(
                {
                    fromTransport(apiV1Mapper.readValue<IRequest>(frame.readText()))
                    wsSession = this@with
                },
                {
                    val result = apiV1Mapper.writeValueAsString(toTransportRule())
                    // If change request, response is sent to everyone
                    outgoing.send(Frame.Text(result))
                },
                classWsV1,
                "wsV1-handle"
            )

        } catch (_: ClosedReceiveChannelException) {
            sessions.remove(this@with)
        } finally {
            // Handle finish request
            appSettings.controllerHelper(
                {
                    command = AppCommand.FINISH
                    wsSession = this@with
                },
                { },
                classWsV1,
                "wsV1-finish"
            )
            sessions.remove(this@with)
        }
    }.collect()
}