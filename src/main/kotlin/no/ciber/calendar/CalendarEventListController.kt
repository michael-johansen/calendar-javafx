package no.ciber.calendar

import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleListProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.concurrent.Service
import javafx.concurrent.Worker
import javafx.event.Event
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.CheckBox
import javafx.scene.control.ChoiceBox
import javafx.scene.control.Label
import javafx.scene.control.ListView
import javafx.scene.input.MouseEvent
import java.net.URL
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

    public fun handleCreateClicked() {

    }

    public fun handleEventSelected(event: MouseEvent) {
        val selectedEvent = eventListView?.getSelectionModel()?.getSelectedItem()
        if (selectedEvent != null) {
            Event.fireEvent(event.getTarget(), NavigateToCalendarEventDetails(selectedEvent))
        }
    }
}

enum class SearchMode {
    All
    Today
    Upcoming
    Past
}
