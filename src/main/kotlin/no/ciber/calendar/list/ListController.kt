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
import no.ciber.calendar.model.Location
import no.ciber.calendar.model.SearchMode
import java.net.URL
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit
import java.util.Locale
import java.util.ResourceBundle

class ListController(val locale:Locale, val searchMode: SearchMode) : Initializable {
    FXML var eventListView: ListView<CalendarEvent>? = null
    FXML var taskRunningIndicator: CheckBox? = null
    FXML var loadingLabel: Label? = null
    FXML var searchModeChoiceBox: ChoiceBox<SearchMode>? = null
    FXML var localeChoiceBox: ChoiceBox<Locale>? = null

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        eventListView!!.itemsProperty().bind(FetchEventListService.valueProperty())
        eventListView!!.visibleProperty().bind(Bindings.not(FetchEventListService.runningProperty()))
        eventListView!!.setCellFactory { CalendarEventListCell() }
        taskRunningIndicator!!.selectedProperty().bind(FetchEventListService.runningProperty())
        loadingLabel!!.visibleProperty().bind(FetchEventListService.runningProperty())

        searchModeChoiceBox!!.setItems(FXCollections.observableArrayList(SearchMode.values().toList()))
        searchModeChoiceBox!!.getSelectionModel().select(searchMode)
        searchModeChoiceBox!!.getSelectionModel().selectedItemProperty().addListener({
            observable, oldValue, newValue ->
            handleUpdateClicked()
        })
        FetchEventListService.searchModeProperty.bind(searchModeChoiceBox!!.valueProperty())


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
        FetchEventListService.getValue()?.clear()
        if (FetchEventListService.isRunning()) FetchEventListService.cancel()
        if (FetchEventListService.getState() != Worker.State.READY) FetchEventListService.reset()
        FetchEventListService.start()
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
        calendarEvent.location = null;
        calendarEvent.createdDate = Settings.eventDateFormat().format(now.atOffset(zoneOffset))
        calendarEvent.startDate = Settings.eventDateFormat().format(now.atOffset(zoneOffset))
        calendarEvent.endDate = Settings.eventDateFormat().format(now.plus(2, ChronoUnit.HOURS).atOffset(zoneOffset))
        Event.fireEvent(event.getTarget(), NavigateToCalendarEventDetails(calendarEvent))
    }
}