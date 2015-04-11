package no.ciber.calendar

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.CollectionType
import com.fasterxml.jackson.databind.type.SimpleType
import com.mashape.unirest.http.Unirest
import com.mashape.unirest.http.exceptions.UnirestException
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.concurrent.Service
import javafx.concurrent.Task
import org.slf4j.LoggerFactory


class FetchEventListService : Service<ObservableList<CalendarEvent>>() {
    val logger = LoggerFactory.getLogger(javaClass)

    override fun createTask(): Task<ObservableList<CalendarEvent>> {
        logger.info("Creating new task")
        return object:Task<ObservableList<CalendarEvent>>(){
            override fun call(): ObservableList<CalendarEvent> {
                try{
                    return getEvents()
                }catch(e:Exception){
                    logger.warn("Could not fetch events:", e)
                    throw e
                }
            }
        }
    }

    private fun getEvents(): ObservableList<CalendarEvent> {
        logger.info("Fetching events")
        val request = Unirest.get(Settings.eventServiceUrl)
        val response = request.asString()
        if (response.getStatus() != 200) throw UnirestException("Not OK!")
        val json = response.getBody()
        val events = parseJson(json)
        val observableList = FXCollections.observableArrayList(events)
        logger.info("Done fetching events")
        return observableList
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