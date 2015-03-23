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
import javafx.concurrent.Worker
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.input.MouseEvent
import java.awt
import java.net.URL
import java.util.ResourceBundle
import java.util.function.Consumer

class EventListController(application: CalendarApplication) : BaseController(application), Initializable {
    [FXML]
    private var eventListView: ListView<Event>? = null
    [FXML]
    private var taskRunningIndicator: CheckBox? = null
    [FXML]
    private var loadingLabel: Label? = null

    var getEventsService: Service<ObservableList<Event>> = GetEventsService()

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        eventListView!!.itemsProperty().bind(getEventsService.valueProperty())
        eventListView!!.visibleProperty().bind(Bindings.not(getEventsService.runningProperty()))
        eventListView!!.setCellFactory {
            object : ListCell<Event>() {
                override fun updateItem(item: Event?, empty: Boolean) {
                    super.updateItem(item, empty)
                    if (item != null && !empty) {
                        textProperty().bind(item.textProperty())
                    }
                }
            }
        }
        taskRunningIndicator!!.selectedProperty().bind(getEventsService.runningProperty())
        loadingLabel!!.visibleProperty().bind(getEventsService.runningProperty())

        handleUpdateClicked()
    }

    public fun handleUpdateClicked() {
        if (getEventsService.isRunning()) getEventsService.cancel()
        if (getEventsService.getState() != Worker.State.READY) getEventsService.reset()
        getEventsService.start()

    }


    public fun handleEventSelected(event: MouseEvent) {
        gotoScene<EventDetailController>(
                fxml = "event-details.fxml",
                callback = {(controller) -> println(event.getTarget()) }
        )
    }
}
