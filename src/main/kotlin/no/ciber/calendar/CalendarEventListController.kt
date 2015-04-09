package no.ciber.calendar

import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleListProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.concurrent.Service
import javafx.concurrent.Worker
import javafx.event.ActionEvent
import javafx.event.Event
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.CheckBox
import javafx.scene.control.ChoiceBox
import javafx.scene.control.Label
import javafx.scene.control.ListView
import javafx.scene.input.MouseEvent
import java.net.URL
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters
import java.util.ArrayList
import java.util.ResourceBundle

class CalendarEventListController : Initializable {
    FXML var eventListView: ListView<CalendarEvent>? = null
    FXML var taskRunningIndicator: CheckBox? = null
    FXML var loadingLabel: Label? = null
    FXML var searchMode: ChoiceBox<SearchMode>? = null

    val fetchEventListService = FetchEventListService()

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        eventListView!!.itemsProperty().bind(fetchEventListService.valueProperty())
        eventListView!!.visibleProperty().bind(Bindings.not(fetchEventListService.runningProperty()))
        eventListView!!.setCellFactory { CalendarEventListCell() }
        taskRunningIndicator!!.selectedProperty().bind(fetchEventListService.runningProperty())
        loadingLabel!!.visibleProperty().bind(fetchEventListService.runningProperty())


        val simpleListProperty = FXCollections.observableArrayList(SearchMode.values().toList())
        searchMode!!.setItems(simpleListProperty)
        searchMode!!.getSelectionModel().select(SearchMode.Upcoming)


        handleUpdateClicked()
    }

    public fun handleUpdateClicked() {
        if (fetchEventListService.isRunning()) fetchEventListService.cancel()
        if (fetchEventListService.getState() != Worker.State.READY) fetchEventListService.reset()
        fetchEventListService.start()
    }

    public fun handleEventSelected(event: MouseEvent) {
        val selectedEvent = eventListView?.getSelectionModel()?.getSelectedItem()
        if (selectedEvent != null) {
            Event.fireEvent(event.getTarget(), NavigateToCalendarEventDetails(selectedEvent))
        }
    }

    public fun handleCreateClicked(event: ActionEvent) {
        val calendarEvent = CalendarEvent()
        val now = LocalDateTime.now()
        val zoneOffset = ZoneOffset.ofHours(0)
        calendarEvent.startDate = DateTimeFormatter.ISO_INSTANT.format(now.toInstant(zoneOffset))
        calendarEvent.endDate = DateTimeFormatter.ISO_INSTANT.format(now.plus(2, ChronoUnit.HOURS).toInstant(zoneOffset))
        Event.fireEvent(event.getTarget(), NavigateToCalendarEventDetails(calendarEvent))
    }
}

