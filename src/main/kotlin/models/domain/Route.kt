package org.sebastianv.models.domain

import kotlinx.serialization.Serializable

@Serializable
data class Route(
    val origin: String,
    val destination: String,
    val fare: Double
)