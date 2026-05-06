package org.sebastianv.models.dto

import org.sebastianv.models.domain.PassengerType

data class FareRequest(
    val origin: String,
    val destination: String,
    val passengerType: PassengerType,
    val journeyDate: String
)