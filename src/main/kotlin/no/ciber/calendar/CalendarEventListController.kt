package no.ciber.calendar

import javafx.beans.binding.Bindings
import javafx.collections.ObservableList
import javafx.concurrent.Service
import javafx.concurrent.Worker
import javafx.event.Event
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.CheckBox
import javafx.scene.control.Label
import javafx.scene.control.ListView
import javafx.scene.input.MouseEvent
import java.net.URL
import java.util.ResourceBundle

class CalendarEventListController : Initializable {
    [FXML]
    private var eventListView: ListView<CalendarEvent>? = null
    [FXML]
    private var taskRunningIndicator: CheckBox? = null
    [FXML]
    private var loadingLabel: Label? = null

    val fetchEventListService = FetchEventListService()

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        eventListView!!.itemsProperty().bind(fetchEventListService.valueProperty())
        eventListView!!.visibleProperty().bind(Bindings.not(fetchEventListService.runningProperty()))
        eventListView!!.setCellFactory { CalendarEventListCell() }
        taskRunningIndicator!!.selectedProperty().bind(fetchEventListService.runningProperty())
        loadingLabel!!.visibleProperty().bind(fetchEventListService.runningProperty())

        handleUpdateClicked()
    }

    public fun handleUpdateClicked() {
        if (fetchEventListService.isRunning()) fetchEventListService.cancel()
        if (fetchEventListService.getState() != Worker.State.READY) fetchEventListService.reset()
        fetchEventListService.start()
    }

    public fun handleEventSelected(event: MouseEvent) {
        val selectedEvent = eventListView?.getSelectionModel()?.getSelectedItem()
        if(selectedEvent!=null){
            Event.fireEvent(event.getTarget(), NavigateToCalendarEventDetails(selectedEvent))
        }
    }
}
