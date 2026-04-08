package org.sebastianv

class FareProcessingService (
    private val routeRepository: RouteRepositoryPort
) {
    //TODO: Decide how to store the data
    //TODO: Create FareProcessingUseCase

    /*val tariffSheet = mapOf(
        "London" to mapOf(
            "Paris" to 100,
            "Madrid" to 200
        ),
        "Paris" to mapOf(
            "Madrid" to 200,
            "London" to 100
        ),
        "Madrid" to mapOf(
            "Paris" to 200,
            "London" to 200,
        )                   }
    )*/

    fun calculate(fareRequest: FareRequest): Double {
        return calculateFare(fareRequest)
    }

    private fun calculateFare(fareRequest: FareRequest): Double {
        val baseFare = routeRepository.findByOriginAndDestination(fareRequest.origin, fareRequest.destination)?.fare
        if (baseFare != null){
            val fare = applyPassengerDiscount(baseFare, fareRequest.passengerType)
            return fare
        }
        return 0.0 //TODO: return an error with FareResult
    }

    private fun applyPassengerDiscount (baseFare: Double, passengerType: PassengerType): Double{
        return when (passengerType) {
            is PassengerType.Adult -> baseFare
            is PassengerType.Child -> baseFare * 0.5
            is PassengerType.Senior -> if (passengerType.age >= 65) baseFare * 0.7 else baseFare
        }
    }
}