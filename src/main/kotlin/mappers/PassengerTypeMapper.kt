package org.sebastianv.mappers

import org.sebastianv.models.domain.PassengerType

fun String.toPassengerType(age: Int): PassengerType {
    return when (this.uppercase()) {
        "ADULT" -> PassengerType.Adult
        "CHILD" -> PassengerType.Child
        "SENIOR" -> PassengerType.Senior(age)
        else -> throw IllegalArgumentException("Invalid information submitted")
    }
}
