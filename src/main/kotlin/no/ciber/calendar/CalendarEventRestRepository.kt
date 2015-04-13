package no.ciber.calendar;

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.type.CollectionType
import com.fasterxml.jackson.databind.type.SimpleType
import com.mashape.unirest.http.HttpResponse
import com.mashape.unirest.http.Unirest
import com.mashape.unirest.http.exceptions.UnirestException
import no.ciber.calendar.model.CalendarEvent
import no.ciber.calendar.model.SearchMode
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.ZonedDateTime

/**
 * User: Michael Johansen
 * Date: 13.04.2015
 * Time: 15:50
 */
public object CalendarEventRestRepository {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    public fun list(searchMode: SearchMode?): List<CalendarEvent> {
        logger.info("Fetching $searchMode events")
        val request = Unirest.get(Settings.eventServiceUrl)
        when (searchMode) {
            SearchMode.Past -> request.queryString("intervalEnd", Settings.eventDateFormat().format(ZonedDateTime.now()))
            SearchMode.Today -> {
                val now = ZonedDateTime.now()
                val startOfToday = now.withHour(0).withMinute(0).withSecond(0)
                val startOfTomorrow = startOfToday.plusDays(1)
                request.queryString("intervalStart", Settings.eventDateFormat().format(startOfToday))
                request.queryString("intervalEnd", Settings.eventDateFormat().format(startOfTomorrow))
            }
            SearchMode.Upcoming -> request.queryString("intervalStart", Settings.eventDateFormat().format(ZonedDateTime.now()))
        }
        logger.info("Fetching ${request.getHttpRequest().getUrl()}")

        val response = request.asString()
        if (response.getStatus() != 200) throw UnirestException("Not OK!")
        val json = response.getBody()
        val events = parseJson(json)
        logger.info("Done fetching events")
        return events
    }

    public fun save(event:CalendarEvent){
        val mapper = ObjectMapper()
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        val jsonString = mapper.writeValueAsString(event)
        println("Posting:\n $jsonString")

        val request = if (event.id == null) Unirest.post(Settings.eventServiceUrl) else Unirest.put("${Settings.eventServiceUrl}/${event.id}");
        request.header("Content-Type", "application/json")
        request.body(jsonString)
        val jsonStringResponse = request.asString()

        printResponse(jsonStringResponse)

        val jsonNode: JsonNode = mapper.readTree(jsonStringResponse.getBody())
        println(mapper.writeValueAsString(jsonNode))
    }

    public fun delete(event:CalendarEvent){
            val request = Unirest.delete("${Settings.eventServiceUrl}/${event.id}")
            val response = request.asString()
            printResponse(response)
            if(!(response.getStatus() in 200..299)){
                throw IllegalStateException("Could not delete event: ${event}")
            }
    }


    private fun printResponse(jsonStringResponse: HttpResponse<String>) {
        println("Response:")
        println("${jsonStringResponse.getStatus()} - ${jsonStringResponse.getStatusText()}")
    }

    private fun parseJson(json: String): kotlin.List<CalendarEvent> {
        val mapper = ObjectMapper()
        mapper.findAndRegisterModules()

        val javaType: CollectionType = CollectionType.construct(
                javaClass<java.util.List<Any>>(),
                SimpleType.construct(javaClass<CalendarEvent>())
        )
        val readValue: java.util.List<CalendarEvent> = mapper.readValue(json, javaType)
        return readValue as kotlin.List<CalendarEvent>

    }
}
