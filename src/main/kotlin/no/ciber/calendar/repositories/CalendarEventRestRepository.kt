package no.ciber.calendar.repositories

import com.fasterxml.jackson.databind.ObjectMapper
import com.mashape.unirest.http.HttpResponse
import com.mashape.unirest.http.Unirest
import com.mashape.unirest.http.exceptions.UnirestException
import com.mashape.unirest.request.HttpRequest
import com.mashape.unirest.request.HttpRequestWithBody
import no.ciber.calendar.Settings
import no.ciber.calendar.model.CalendarEvent
import no.ciber.calendar.model.SearchMode
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.*
import java.util.*

/**
 * User: Michael Johansen
 * Date: 13.04.2015
 * Time: 15:50
 */
public object CalendarEventRestRepository {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)
    private val mapper = ObjectMapper();;

    public fun list(searchMode: SearchMode?): List<CalendarEvent> {
        logger.info("Fetching $searchMode events")
        val request = Unirest.get(Settings.eventServiceUrl + "events")
        when (searchMode) {
            SearchMode.Past -> request.queryString("intervalEnd", Instant.now().toEpochMilli())
            SearchMode.Today -> {
                val now = ZonedDateTime.now()
                val startOfToday = now.withHour(0).withMinute(0).withSecond(0)
                val startOfTomorrow = startOfToday.plusDays(1)
                request.queryString("intervalStart", startOfToday.toInstant().toEpochMilli())
                request.queryString("intervalEnd", startOfTomorrow.toInstant().toEpochMilli())
            }
            SearchMode.Upcoming -> request.queryString("intervalStart", Instant.now().toEpochMilli())
        }

        if(Settings.useMockApi()){
            return listOf(
                    calendarEvent("1", "Awesome event"),
                    calendarEvent("2", "BIL Poker"),
                    calendarEvent("3", "Java fagdag")
            )
        }

        logRequest(request)

        val response = request.asString()
        if (response.getStatus() != 200) throw UnirestException("Not OK!")
        logResponse(response)
        return parseJson(response.getBody())
    }

    private fun calendarEvent(id: String, name: String): CalendarEvent {
        val event = CalendarEvent()
        event.id = id
        event.createdDate = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
        event.startDate = LocalDateTime.now().plusHours(1).toEpochSecond(ZoneOffset.UTC)
        event.endDate = LocalDateTime.now().plusHours(2).toEpochSecond(ZoneOffset.UTC)
        event.description = "Be there or be square"
        event.name = name
        return event
    }

    public fun save(event: CalendarEvent) {
        val jsonString = mapper.writeValueAsString(event)

        val request = if (event.id == null) Unirest.post("${Settings.eventServiceUrl}events") else Unirest.put("${Settings.eventServiceUrl}events/${event.id}");
        request.header("Content-Type", "application/json")
        request.body(jsonString)

        logRequest(request, jsonString)
        logResponse(request.asString())
    }

    public fun delete(event: CalendarEvent) {
        val request = Unirest.delete("${Settings.eventServiceUrl}events/${event.id}")
        logRequest(request)
        val response = request.asString()
        logResponse(response)
        if (!(response.getStatus() in 200..299)) {
            throw IllegalStateException("Could not delete event: ${event}")
        }
    }

    private fun logRequest(request: HttpRequest, body:String? = null) {
        logger.info("${request.getHttpMethod()} ${request.getHttpRequest().getUrl()}")
        logger.info("data: ${body}")
    }


    private fun logResponse(jsonStringResponse: HttpResponse<String>) {
        logger.info("Response:")
        logger.info("${jsonStringResponse.getStatus()} - ${jsonStringResponse.getStatusText()}")
        logger.info("${jsonStringResponse.getBody()}")
    }

    private fun parseJson(json: String): List<CalendarEvent> {
        val clazz = javaClass<Array<CalendarEvent>>()
        val arrayOfCalendarEvents = mapper.readValue(json, clazz)
        return arrayOfCalendarEvents.toList()
    }
}