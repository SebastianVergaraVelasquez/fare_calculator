package org.sebastianv.handlers

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import org.sebastianv.models.dto.FareRequest
import org.sebastianv.services.FareProcessingService

class FareHandler (
    private val fareProcessingService: FareProcessingService
) {
    suspend fun calculateFare(call: ApplicationCall) {
        try {
            val request = call.receive<FareRequest>()
            val result = fareProcessingService.calculate(request)
            call.respond(result)
        }
        catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, "Invalid request body")
        }
    }

    suspend fun listRoutes(call: ApplicationCall) {
        val allRoutes = fareProcessingService.listRoutes()
        call.respond(allRoutes)
    }

    suspend fun helloWorld(call: ApplicationCall) {
        call.respond("Hello World!")
    }
}