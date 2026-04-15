package org.sebastianv
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class RouteRepositoryMySQL: RouteRepositoryPort {

    object RouteTable : Table("routes") {
        val origin = varchar("origin", 50)
        val destination = varchar("destination", 50)
        val fare = double("fare")
    }

    override fun findByOriginAndDestination(origin: String, destination: String): Route? {
        TODO("Not yet implemented")
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