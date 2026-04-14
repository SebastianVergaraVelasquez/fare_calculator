package org.sebastianv

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(fareProcessingService: FareProcessingService) {
    routing {
        get("/") {
            call.respondText("Fare Calculator 🚀")
        }
        route("/choose-route") {
            get {
                val params = call.request.queryParameters

                val origin = params["origin"]
                val destination = params["destination"]
                val passengerTypeParam = params["passengerType"]
                val journeyDate = params["journeyDate"]
                val ageParam = params["age"]

                if (origin == null || destination == null || passengerTypeParam == null || journeyDate == null || ageParam == null) {
                    call.respondText("Invalid operation", status = io.ktor.http.HttpStatusCode.BadRequest)
                    return@get
                }

                val age = ageParam.toIntOrNull()
                if (age == null) {
                    call.respondText("Invalid operation", status = HttpStatusCode.BadRequest)
                    return@get
                }

                val passengerType = try {
                    passengerTypeParam.toPassengerType(age)
                } catch (e: IllegalArgumentException) {
                    call.respondText(e.message ?: "Invalid passenger type", status = HttpStatusCode.BadRequest)
                    return@get
                }

                val result = fareProcessingService.calculate(
                    FareRequest(origin, destination, passengerType, journeyDate)
                )
                call.respond(result)
                /*call.respondText(result.toString())*/
            }
        }
        route("/routes") {
            get {
                val allFares = fareProcessingService.listRoutes()
                call.respond(allFares)
            }
        }
    }
}