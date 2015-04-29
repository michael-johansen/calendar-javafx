package no.ciber.calendar.repositories

import com.fasterxml.jackson.databind.ObjectMapper
import com.mashape.unirest.http.Unirest
import no.ciber.calendar.Settings
import no.ciber.calendar.model.Location
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object LocationRepository : RestRepository() {
    fun list(): List<Location> {
        return get("${Settings.eventServiceUrl}/locations", javaClass<Array<Location>>()).toList()
    }
}