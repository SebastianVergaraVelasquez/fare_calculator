package org.sebastianv

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.sebastianv.handlers.FareHandler
import org.sebastianv.services.FareProcessingService
import org.sebastianv.mappers.toPassengerType
import org.sebastianv.models.dto.FareRequest

fun Application.configureRouting(fareHandler: FareHandler) {
    routing {
        get("/") {
            fareHandler.helloWorld(call)
        }

        post("/choose-route") {
            fareHandler.calculateFare(call)
        }

        get("/routes") {
            fareHandler.listRoutes(call)
        }
    }
}