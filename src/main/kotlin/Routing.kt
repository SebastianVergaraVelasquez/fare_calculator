package org.sebastianv

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(fareProcessingService: FareProcessingService) {
    routing {
        get("/") {
            call.respondText("Fare Calculator 🚀")
        }
        route("/fare") {
            get {
                val params = call.request.queryParameters

                val origin = params["origin"]
                val destination = params["destination"]
                val passengerTypeParam = params["passengerType"]
                val journeyDate = params["journeyDate"]
                val age = params["age"]

                if (origin == null || destination == null || passengerTypeParam == null || journeyDate == null || age == null)  {
                    call.respondText("Missing parameters", status = io.ktor.http.HttpStatusCode.BadRequest)
                    return@get
                }

                val passengerType = try {
                    passengerTypeParam.toPassengerType(age.toInt())
                } catch (e: IllegalArgumentException) {
                    call.respondText(e.message ?: "Invalid passenger type", status = io.ktor.http.HttpStatusCode.BadRequest)
                    return@get
                }

                val result = fareProcessingService.calculate(
                    FareRequest(origin, destination, passengerType, journeyDate)
                )
                call.respond(result)
                /*call.respondText(result.toString())*/
            }
        }
    }
}