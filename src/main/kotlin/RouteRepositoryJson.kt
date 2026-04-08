package org.sebastianv
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import java.io.File

class RouteRepositoryJson(
    private val filePath: String = "data/routeData.json"
) : RouteRepositoryPort {

    private val json = Json { prettyPrint = true }

    private val routes: MutableList<Route> by lazy {
        loadRoutes().toMutableList()
    }

    private fun loadRoutes(): List<Route> {
        val file = File(filePath)

        if (!file.exists()) {
            file.parentFile.mkdirs()
            file.writeText("[]")
        }

        return json.decodeFromString(file.readText())
    }

    private fun persist() {
        val file = File(filePath)
        file.writeText(json.encodeToString(routes))
    }

    override fun findByOriginAndDestination(origin: String, destination: String): Route? {
        return routes.find {
            it.origin == origin && it.destination == destination
        }
    }

    override fun findAll(): List<Route> = routes

    override fun save(route: Route) {
        routes.add(route)
        persist()
    }
}