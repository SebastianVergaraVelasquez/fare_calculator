package org.sebastianv

import kotlinx.serialization.Serializable

@Serializable
sealed class FareResult {
    @Serializable
    data class Success(val amount: Double, val currency: String) : FareResult()
    @Serializable
    data class Error(val message: String) : FareResult()
}