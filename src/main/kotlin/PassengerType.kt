package org.sebastianv

sealed class PassengerType {
    object Adult : PassengerType()
    object Child : PassengerType()
    data class Senior(val age: Int) : PassengerType()
}
