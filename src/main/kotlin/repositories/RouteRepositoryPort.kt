package org.sebastianv.repositories

import org.sebastianv.models.domain.Route

interface RouteRepositoryPort {
    fun findByOriginAndDestination(
        origin: String,
        destination: String
    ): Route?
    fun findAll(): List<Route>
    fun save(route: Route)
}