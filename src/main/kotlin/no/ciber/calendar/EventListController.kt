package no.ciber.calendar

import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.CollectionLikeType
import com.fasterxml.jackson.databind.type.CollectionType
import com.fasterxml.jackson.databind.type.SimpleType
import com.mashape.unirest.http.HttpResponse
import com.mashape.unirest.http.Unirest
import com.mashape.unirest.http.async.Callback
import com.mashape.unirest.http.exceptions.UnirestException
import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.ListView
import javafx.scene.input.MouseEvent
import java.net.URL
import java.util.ResourceBundle

class EventListController : Initializable {
    [FXML]
    private var eventListView: ListView<String> = ListView()
    private var eventList: ObservableList<String> = FXCollections.observableArrayList()

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        eventListView.setItems(eventList)
        handleUpdateClicked()
    }

    public fun handleUpdateClicked() {
        eventList.clear()
        eventList.add("Loading events...")
        eventListView.setItems(eventList)

        val getRequest = Unirest.get("https://event-service.herokuapp.com")
        getRequest.asStringAsync (object : Callback<String> {
            override fun completed(response: HttpResponse<String>) {
                if (response.getStatus() != 200) return failed(UnirestException("Not OK!"))
                val json = response.getBody()
                val events = parseJson(json)
                setEvents(events)
            }

            override fun failed(e: UnirestException?) {
                setEvents(arrayListOf(createEvent("Could not fetch events...")))
            }

            override fun cancelled() {
                setEvents(arrayListOf(createEvent("Could not fetch events...")))
            }
        })
    }

    public fun handleEventSelected(event : MouseEvent){

    }


    private fun createEvent(name: String) = Event(name = name)

    private fun parseJson(json: String): kotlin.List<Event> {
        val mapper = ObjectMapper()

        val javaType: CollectionType = CollectionType.construct(javaClass<java.util.List<Any>>(), SimpleType.construct(javaClass<Event>()))
        val readValue: java.util.List<Event> = mapper.readValue(json, javaType)
        return readValue as kotlin.List<Event>

    }

    private fun setEvents(events: List<Event>) {
        Platform.runLater {
            eventList.clear()
            events.forEach {
                if (it.description != null) eventList.add(it.description)
            }
        }
    }


}
