package no.ciber.calendar

import javafx.event
import javafx.event.ActionEvent
import javafx.event.Event
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import java.net.URL
import java.util.ResourceBundle

class CalendarEventDetailController(val event: CalendarEvent) :Initializable {
    [FXML]
    private val eventLabel = Label();

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        event.name
    }

    public fun signUp() {

    }

    public fun goBack(event:Event) {
        Event.fireEvent(event.getTarget(), NavigateToCalendarEventList())
    }
}