package org.sebastianv

interface RouteRepositoryPort {
    fun findByOriginAndDestination(
        origin: String,
        destination: String
    ): Route?
    fun findAll(): List<Route>
    fun save(route: Route)
}