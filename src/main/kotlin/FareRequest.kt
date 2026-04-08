package org.sebastianv

data class FareRequest(
    val origin: String,
    val destination: String,
    val passengerType: PassengerType,
    val journeyDate: String
)