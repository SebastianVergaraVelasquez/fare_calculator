package org.sebastianv

import kotlinx.serialization.Serializable

@Serializable
data class Route(
    val origin: String,
    val destination: String,
    val fare: Double
)
