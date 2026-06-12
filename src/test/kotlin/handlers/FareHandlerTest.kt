package handlers

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.testing.testApplication
import io.mockk.every
import io.mockk.mockk
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.sebastianv.configureRouting
import org.sebastianv.configureSerialization
import org.sebastianv.handlers.FareHandler
import org.sebastianv.models.domain.Route
import org.sebastianv.models.dto.FareRequest
import org.sebastianv.repositories.RouteRepositoryMySQL.RouteTable.origin
import org.sebastianv.repositories.RouteRepositoryPort
import org.sebastianv.services.FareProcessingService
import kotlin.test.assertEquals

class FareHandlerTest {

    @Nested
    inner class WhenGettingHelloWorld {
        lateinit var routeRepository: RouteRepositoryPort
        lateinit var fareProcessingService: FareProcessingService
        lateinit var fareHandler: FareHandler

        @BeforeEach
        fun setup() {
            routeRepository = mockk<RouteRepositoryPort>()
            fareProcessingService = FareProcessingService(routeRepository)
            fareHandler = FareHandler(fareProcessingService)
        }

        @Test
        fun testHelloWorld() = testApplication {
            application {
                configureRouting(fareHandler = fareHandler)
            }
            val response = client.get("/")
            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals("Hello World!", response.bodyAsText())
        }
    }

    @Nested
    inner class WhenGettingRoutes {
        lateinit var routeRepository: RouteRepositoryPort
        lateinit var fareProcessingService: FareProcessingService
        lateinit var fareHandler: FareHandler

        @BeforeEach
        fun setup() {
            routeRepository = mockk<RouteRepositoryPort>()
            fareProcessingService = FareProcessingService(routeRepository)
            fareHandler = FareHandler(fareProcessingService)
        }

        @Test
        fun testListRoutes() {
            val route1 = Route(origin = "Miami", destination = "Dallas", fare = 100.0)
            val route2 = Route(origin = "Dallas", destination = "Miami", fare = 100.0)
            val routes = listOf(route1, route2)
            every { routeRepository.findAll() } returns routes
            testApplication {
                application {
                    configureSerialization()
                    configureRouting(fareHandler = fareHandler)
                }
                val response = client.get("/routes")
                assertEquals(Json.encodeToString(routes), response.bodyAsText())
            }
        }
    }

    @Nested
    inner class WhenCalculatingFare {
        lateinit var routeRepository: RouteRepositoryPort
        lateinit var fareProcessingService: FareProcessingService
        lateinit var fareHandler: FareHandler


        @BeforeEach
        fun setup() {
            routeRepository = mockk<RouteRepositoryPort>()
            fareProcessingService = FareProcessingService(routeRepository)
            fareHandler = FareHandler(fareProcessingService)
        }

        @Test
        fun testCalculateFare() {
            val route = Route(origin = "Miami", destination = "Dallas", fare = 100.0)
            every { routeRepository.findByOriginAndDestination("Miami", "Dallas") } returns route
            testApplication(){
                val client = createClient {
                    install(ContentNegotiation) {
                        json()
                    }
                }
                application {
                    configureSerialization()
                    configureRouting(fareHandler = fareHandler)
                }
                val response = client.post("/choose-route") {
                    contentType(ContentType.Application.Json)
                    setBody(FareRequest("Miami", "Dallas", "Adult", "2026-12-05", 30 ))
                }
                assertEquals("""{"type":"org.sebastianv.models.dto.FareResult.Success","amount":100.0,"currency":"USD"}""", response.bodyAsText())
            }
        }
    }
}