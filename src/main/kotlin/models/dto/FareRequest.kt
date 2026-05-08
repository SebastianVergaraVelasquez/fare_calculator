package org.sebastianv.models.dto
import kotlinx.serialization.Serializable

@Serializable
data class FareRequest(
    val origin: String,
    val destination: String,
    val passengerType: String,
    val journeyDate: String,
    val age: Int
)