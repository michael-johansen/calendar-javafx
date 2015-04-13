package no.ciber.calendar.list

import javafx.beans.binding.Bindings
import javafx.collections.FXCollections
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
import no.ciber.calendar.*
import no.ciber.calendar.model.CalendarEvent
import no.ciber.calendar.model.SearchMode
import java.net.URL
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit
import java.util.Locale
import java.util.ResourceBundle

class CalendarEventListController(val locale:Locale) : Initializable {
    FXML var eventListView: ListView<CalendarEvent>? = null
    FXML var taskRunningIndicator: CheckBox? = null
    FXML var loadingLabel: Label? = null
    FXML var searchMode: ChoiceBox<SearchMode>? = null
    FXML var localeChoiceBox: ChoiceBox<Locale>? = null

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

        localeChoiceBox!!.setItems(FXCollections.observableList(listOf(Locale.ENGLISH, Locale("no"))))
        localeChoiceBox!!.getSelectionModel().select(locale)
        localeChoiceBox!!.getSelectionModel().selectedItemProperty().addListener({
            observable, oldValue, newValue ->
            Event.fireEvent(localeChoiceBox, ChangeLocale(newValue))
            Event.fireEvent(localeChoiceBox, NavigateToCalendarEventList())
        })

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
        calendarEvent.name = "My super-sweet event"
        calendarEvent.description = "This will be an awesome event! Be there or be square."
        calendarEvent.location = "Somewhere! Here? There? Everywhere!"
        calendarEvent.createdDate = Settings.eventDateFormat().format(now.atOffset(zoneOffset))
        calendarEvent.startDate = Settings.eventDateFormat().format(now.atOffset(zoneOffset))
        calendarEvent.endDate = Settings.eventDateFormat().format(now.plus(2, ChronoUnit.HOURS).atOffset(zoneOffset))
        Event.fireEvent(event.getTarget(), NavigateToCalendarEventDetails(calendarEvent))
    }
}