package org.sebastianv.services

import org.sebastianv.mappers.toPassengerType
import org.sebastianv.models.dto.FareRequest
import org.sebastianv.models.dto.FareResult
import org.sebastianv.models.domain.PassengerType
import org.sebastianv.models.domain.Route
import org.sebastianv.repositories.RouteRepositoryPort
import java.time.LocalDate

class FareProcessingService (
    private val routeRepository: RouteRepositoryPort
) {
    fun calculate(fareRequest: FareRequest): FareResult {
        return calculateFare(fareRequest)
    }

    fun listRoutes(): List<Route> {
        return routeRepository.findAll()
    }

    private fun calculateFare(fareRequest: FareRequest): FareResult {
        if (!validateBodyRequest(fareRequest) || !validateAge(fareRequest.age) || !validateJourneyDate(fareRequest.journeyDate)) {
            return FareResult.Error("Invalid request body")
        }
        val origin = firstLetterToUppercase(fareRequest.origin)
        val destination = firstLetterToUppercase(fareRequest.destination)
        val passengerType = validatePassengerType(fareRequest.passengerType, fareRequest.age)
        val baseFare = calculateBaseFare(origin, destination)
        return FareResult.Success(applyPassengerDiscount(baseFare, passengerType), "USD")
    }

    private fun calculateBaseFare(origin: String, destination: String): Double {
        val route = requireNotNull(
            routeRepository.findByOriginAndDestination(origin, destination)
        ) { "Route not found" }
        return route.fare
    }

    private fun applyPassengerDiscount (baseFare: Double, passengerType: PassengerType): Double{
        return when (passengerType) {
            is PassengerType.Adult -> baseFare
            is PassengerType.Child -> baseFare * 0.5
            is PassengerType.Senior -> if (passengerType.age >= 65) baseFare * 0.7 else baseFare
        }
    }

    private fun validateBodyRequest(fareRequest: FareRequest): Boolean {
        return !(fareRequest.origin.isEmpty() || fareRequest.destination.isEmpty() || fareRequest.passengerType.isEmpty()
                || fareRequest.journeyDate.isEmpty() || fareRequest.age == 0)
    }

    private fun firstLetterToUppercase(str: String): String {
        return str.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }

    private fun validateAge(age: Int): Boolean {
        return age in 0..120
    }

    private fun validatePassengerType(type: String, age: Int): PassengerType{
         return type.toPassengerType(age)
    }

    private fun validateJourneyDate(date: String): Boolean {
        val minDate = LocalDate.of(1900, 1, 1)
        val maxDate = LocalDate.now().plusYears(5)
        val parsedDate =  LocalDate.parse(date)
        return !(parsedDate < minDate || parsedDate > maxDate)
    }
}