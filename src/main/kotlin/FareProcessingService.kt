package org.sebastianv

class FareProcessingService (
    private val routeRepository: RouteRepositoryPort
) {
    //TODO: Decide how to store the data
    //TODO: Create FareProcessingUseCase

    fun calculate(fareRequest: FareRequest): FareResult {
        return calculateFare(fareRequest)
    }

    fun listRoutes(): List<Route> {
        return routeRepository.findAll()
    }

    private fun calculateFare(fareRequest: FareRequest): FareResult {
        return when {
            fareRequest.origin.isEmpty() -> FareResult.Error("Origin is required")
            fareRequest.destination.isEmpty() -> FareResult.Error("Destination is required")
            else -> {
                val baseFare = calculateBaseFare(fareRequest.origin, fareRequest.destination)
                val fare = applyPassengerDiscount(baseFare, fareRequest.passengerType)
                FareResult.Success(fare, "USD")
            }
        }
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
}