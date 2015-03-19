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


class GetEventsService : Service<ObservableList<Event>>() {
    override fun createTask(): Task<ObservableList<Event>> {
        return object:Task<ObservableList<Event>>(){
            override fun call(): ObservableList<Event> {
                val request = Unirest.get("https://event-service.herokuapp.com")
                val response = request.asString()
                if (response.getStatus() != 200) throw UnirestException("Not OK!")
                val json = response.getBody()
                val events = parseJson(json)
                return FXCollections.observableArrayList(events)
            }
        }
    }

    private fun parseJson(json: String): kotlin.List<Event> {
        val mapper = ObjectMapper()

        val javaType: CollectionType = CollectionType.construct(
                javaClass<java.util.List<Any>>(),
                SimpleType.construct(javaClass<Event>())
        )
        val readValue: java.util.List<Event> = mapper.readValue(json, javaType)
        return readValue as kotlin.List<Event>

    }

}