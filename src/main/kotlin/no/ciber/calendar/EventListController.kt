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
import javafx.beans.binding.Bindings
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.concurrent.Service
import javafx.concurrent.Task
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Cell
import javafx.scene.control.CheckBox
import javafx.scene.control.ListView
import javafx.scene.input.MouseEvent
import java.awt
import java.net.URL
import java.util.ResourceBundle

class EventListController : Initializable {
    [FXML]
    private var eventListView: ListView<Event> = ListView()
    [FXML]
    private var taskRunningIndicator: CheckBox = CheckBox()

    var getEventsService: Service<ObservableList<Event>> = GetEventsService()

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        eventListView.itemsProperty().bind(getEventsService.valueProperty())
        eventListView.visibleProperty().bind(Bindings.not(getEventsService.runningProperty()))
        taskRunningIndicator.selectedProperty().bind(getEventsService.runningProperty())

        handleUpdateClicked()
    }

    public fun handleUpdateClicked() {
        getEventsService.reset()
        getEventsService.start()

    }

    public fun handleEventSelected(event: MouseEvent) {

    }
}
