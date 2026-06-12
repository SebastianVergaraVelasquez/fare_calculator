package services

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.sebastianv.models.domain.Route
import org.sebastianv.models.dto.FareRequest
import org.sebastianv.models.dto.FareResult
import org.sebastianv.repositories.RouteRepositoryPort
import org.sebastianv.services.FareProcessingService

class FareProcessingServiceTest {

    @Nested
    inner class WhenCalculatingFare {
        lateinit var routeRepository: RouteRepositoryPort
        lateinit var fareProcessingService: FareProcessingService

        @BeforeEach
        fun setup() {
            routeRepository = mockk<RouteRepositoryPort>()
            fareProcessingService = FareProcessingService(routeRepository)

        }
        @Test
        fun `returns full fare for adults`(){
            every { routeRepository.findByOriginAndDestination("Miami","Dallas") } returns Route(
                origin = "Miami",
                destination = "Dallas",
                fare = 100.0
            )
            val fareRequest = FareRequest("miami", "Dallas","Adult", "2026-12-05",30)
            val result = fareProcessingService.calculate(fareRequest)
            val fareResult = FareResult.Success(100.00, "USD")
            assertEquals(fareResult, result)
        }

        @Test
        fun `returns half fare for children`() {
            every {routeRepository.findByOriginAndDestination("Miami", "Dallas") } returns Route(
                origin = "Miami",
                destination = "Dallas",
                fare = 100.0
            )
            val fareRequest = FareRequest("miami", "Dallas", "Child", "2026-12-05", 8)
            val result = fareProcessingService.calculate(fareRequest)
            val fareResult = FareResult.Success(50.00, "USD")
            assertEquals(fareResult, result)
        }

        @Test
        fun `returns 30 percent discount for seniors`() {
            every { routeRepository.findByOriginAndDestination("Miami", "Dallas") } returns Route(
                origin = "Miami",
                destination = "Dallas",
                fare = 100.0
            )
            val fareRequest = FareRequest("miami", "Dallas", "Senior", "2026-12-05", 70)
            val result = fareProcessingService.calculate(fareRequest)
            val fareResult = FareResult.Success(70.00, "USD")
            assertEquals(fareResult, result)
        }

        @Test
        fun `senior with age 64`() {
            every { routeRepository.findByOriginAndDestination("Miami", "Dallas") } returns Route(
                origin = "Miami",
                destination = "Dallas",
                fare = 100.0
            )
            val fareRequest = FareRequest("miami", "Dallas", "Senior", "2026-12-05", 64)
            val result = fareProcessingService.calculate(fareRequest)
            val fareResult = FareResult.Success(100.00, "USD")
            assertEquals(fareResult, result)
        }

        @Test
        fun `senior with age 65`() {
            every { routeRepository.findByOriginAndDestination("Miami", "Dallas") } returns Route(
                origin = "Miami",
                destination = "Dallas",
                fare = 100.0
            )
            val fareRequest = FareRequest("miami", "Dallas", "Senior", "2026-12-05", 65)
            val result = fareProcessingService.calculate(fareRequest)
            val fareResult = FareResult.Success(70.00, "USD")
            assertEquals(fareResult, result)
        }
    }

    @Nested
    inner class WhenGettingRoutes{
        lateinit var routeRepository: RouteRepositoryPort
        lateinit var fareProcessingService: FareProcessingService

        @BeforeEach
        fun setup(){
            routeRepository = mockk<RouteRepositoryPort>()
            fareProcessingService = FareProcessingService(routeRepository)
        }

        @Test
        fun `list all routes`(){
            val route1 = Route(origin = "Miami", destination = "Dallas", fare = 100.0)
            val route2 = Route(origin = "Dallas", destination = "Miami", fare = 100.0)
            val routes = listOf(route1, route2)
            every { routeRepository.findAll() } returns routes
            val result = fareProcessingService.listRoutes()
            assertEquals(routes, result)
        }
    }
}