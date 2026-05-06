package org.sebastianv.repositories

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.sebastianv.repositories.RouteRepositoryPort
import org.sebastianv.models.domain.Route

class RouteRepositoryMySQL: RouteRepositoryPort {

    object RouteTable : Table("routes") {
        val origin = varchar("origin", 50)
        val destination = varchar("destination", 50)
        val fare = double("fare")
    }

    override fun findByOriginAndDestination(origin: String, destination: String): Route? {
        return transaction {
            RouteTable.select {
                (RouteTable.origin eq origin) and (RouteTable.destination eq destination)
            }.map { it.toRoute() }.firstOrNull()
        }
    }

    override fun findAll(): List<Route> {
        return transaction {
            RouteTable.selectAll().map { it.toRoute() }
        }
    }

    override fun save(route: Route) {
        TODO("Not yet implemented")
    }

    private fun ResultRow.toRoute() = Route(
        origin = this[RouteTable.origin],
        destination = this[RouteTable.destination],
        fare = this[RouteTable.fare]
    )
}