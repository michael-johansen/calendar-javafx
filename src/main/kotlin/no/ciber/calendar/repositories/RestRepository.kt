package no.ciber.calendar.repositories

import com.fasterxml.jackson.databind.ObjectMapper
import com.mashape.unirest.http.HttpResponse
import com.mashape.unirest.http.Unirest
import com.mashape.unirest.request.HttpRequest
import no.ciber.calendar.Settings
import no.ciber.calendar.model.CalendarEvent
import no.ciber.calendar.model.Location
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class RestRepository {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)
    private val mapper = ObjectMapper()

    fun get<T>(url: String, clazz: Class<out T>): T {
        val getRequest = Unirest.get(url).header("Accept", "application/json")
        logger.info("GET ${getRequest.getUrl()}")

        val response = getRequest.asString()
        logger.info("${response.getStatus()} - ${response.getStatusText()}")
        logger.info("${response.getBody()}")

        return mapper.readValue(response.getBody(), clazz)
    }

    fun post<T>(url: String, data: T) {
        val body = mapper.writeValueAsString(data)
        // aggregator must route this one also
        val getRequest = Unirest.post(url).header("Accept", "application/json")
                .header("ContentType", "application/json")
                .body(body)
        logger.info("${getRequest.getHttpRequest().getHttpMethod()} ${getRequest.getHttpRequest().getUrl()}")
        logger.info("Data: ${body}")

        val response = getRequest.asString()
        logger.info("${response.getStatus()} - ${response.getStatusText()}")
        logger.info("${response.getBody()}")
    }
}