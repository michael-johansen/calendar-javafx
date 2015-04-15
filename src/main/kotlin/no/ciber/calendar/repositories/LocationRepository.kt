package no.ciber.calendar.repositories

import com.fasterxml.jackson.databind.ObjectMapper
import com.mashape.unirest.http.Unirest
import no.ciber.calendar.Settings
import no.ciber.calendar.model.Location
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object LocationRepository {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)
    val mapper = ObjectMapper()

    fun getAllLocations():List<Location> {
        logger.info("Fetching locations")
        val getRequest = Unirest.get("${Settings.eventServiceUrl}/locations")
        getRequest.header("Accept", "application/json")
        logger.info("GET ${getRequest.getUrl()}")

        val response = getRequest.asString()
        logger.info("${response.getStatus()} - ${response.getStatusText()}")
        logger.info("${response.getBody()}")

        val clazz = javaClass<Array<Location>>()
        val readValue :Array<Location> = mapper.readValue(response.getBody(), clazz)
        return readValue.toList()
    }

}