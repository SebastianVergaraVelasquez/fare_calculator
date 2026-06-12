package mappers

import org.sebastianv.models.domain.PassengerType
import org.sebastianv.mappers.toPassengerType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class PassengerTypeMapperTest {
    @Nested
    inner class WhenPassengerIsAdult {

        @Test
        fun `returns Adult when type is ADULT uppercase`() {
            val result = "ADULT".toPassengerType(age = 30)
            assertEquals(PassengerType.Adult, result)
        }

        @ParameterizedTest
        @ValueSource(strings = ["adult", "Adult", "ADULT", "aDuLt"])
        fun `returns Adult regardless of case`(input: String) {
            val result = input.toPassengerType(age = 30)
            assertEquals(PassengerType.Adult, result)
        }
    }

    @Nested
    inner class WhenPassengerIsChild {

        @Test
        fun `returns Child when type is CHILD`() {
            val result = "child".toPassengerType(age = 8)
            assertEquals(PassengerType.Child, result)
        }
    }

    @Nested
    inner class WhenPassengerIsSenior {

        @Test
        fun `returns Senior with correct age`() {
            val result = "senior".toPassengerType(age = 70)
            assertEquals(PassengerType.Senior(age = 70), result)
        }

        @Test
        fun `Senior carries the age passed in`() {
            val result = "SENIOR".toPassengerType(age = 68) as PassengerType.Senior
            assertEquals(68, result.age)
        }
    }

    @Nested
    inner class WhenTypeIsInvalid {

        @Test
        fun `throws IllegalArgumentException for unknown type`() {
            assertThrows<IllegalArgumentException> {
                "UNKNOWN".toPassengerType(age = 30)
            }
        }

        @ParameterizedTest
        @ValueSource(strings = ["", "   ", "admin", "supersenior"])
        fun `throws IllegalArgumentException for any invalid input`(input: String) {
            assertThrows<IllegalArgumentException> {
                input.toPassengerType(age = 30)
            }
        }
    }
}
