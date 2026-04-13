package org.sebastianv

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }
}

fun Application.module() {
    val routeRepository = RouteRepositoryJson()
    val fareProcessingService = FareProcessingService(routeRepository)

    configureSerialization()
    configureRouting(fareProcessingService)
}