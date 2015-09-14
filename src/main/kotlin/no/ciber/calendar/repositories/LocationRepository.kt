package no.ciber.calendar.repositories

import com.fasterxml.jackson.databind.ObjectMapper
import com.mashape.unirest.http.Unirest
import no.ciber.calendar.Settings
import no.ciber.calendar.model.Location
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

object LocationRepository : RestRepository() {
    var localList: MutableList<Location> = ArrayList<Location>()


    fun list(): List<Location> {
        if (Settings.useMockApi()) {
            if (localList.isEmpty()) {
                localList.add(location(UUID.randomUUID().toString(), "Møterom Torget", "Torget"))
                localList.add(location(UUID.randomUUID().toString(), "Møterom Rådhusett", "Rådhuset"))
            }
            return localList
        }
        return get("${Settings.eventServiceUrl}/locations", javaClass<Array<Location>>()).toList()
    }

    private fun location(id: String, description: String, name: String): Location {
        val location = Location()
        location.id = id
        location.location = name
        location.description = description
        location.longitude = 59.9175641
        location.latitude = 10.7532857
        return location
    }

    fun create(location: Location) {
        if(Settings.useMockApi()){
            localList.add(location)
            return
        }
        post("${Settings.eventServiceUrl}/locations", location)
    }
}