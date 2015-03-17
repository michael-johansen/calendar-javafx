package no.ciber.calendar

import java.util.ArrayList
import javafx.fxml.FXML
import javafx.collections.ObservableList
import javafx.beans.property.SimpleListProperty
import javafx.scene.control.ListView
import javafx.fxml.Initializable
import java.net.URL
import java.util.ResourceBundle
import com.mashape.unirest.http.Unirest
import com.mashape.unirest.http.async.Callback
import com.mashape.unirest.http.HttpResponse
import com.mashape.unirest.http.exceptions.UnirestException
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.Collection
import javafx.collections.FXCollections
import javafx.application.Platform

class EventController : Initializable {
    [FXML]
    private var eventListView: ListView<String> = ListView()
    private var eventList: ObservableList<String> = FXCollections.observableArrayList()

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        eventList.add("Loading events...")
        eventListView.setItems(eventList)


        val getRequest = Unirest.get("https://event-service.herokuapp.com")
        getRequest.asStringAsync (object : Callback<String> {
            override fun completed(response: HttpResponse<String>) {
                val collectionType: Type = object : TypeToken<Collection<Event>>() {}.getType()
                val body = response.getBody()
                if (body != null) {
                    val events: Collection<Event> = Gson().fromJson<Collection<Event>>(body, collectionType)
                    Platform.runLater {
                        eventList.clear()
                        for (event in events) {
                            if (event.description != null) {
                                eventList.add(event.description)
                            }
                        }
                    }
                }
            }

            override fun failed(e: UnirestException?) {
            }

            override fun cancelled() {
            }
        })
    }


}
