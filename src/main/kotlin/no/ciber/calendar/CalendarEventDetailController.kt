package no.ciber.calendar

import javafx.event
import javafx.event.ActionEvent
import javafx.event.Event
import javafx.fxml.FXML
import javafx.scene.control.Label

class CalendarEventDetailController {
    [FXML]
    private val eventLabel = Label();

    public fun signUp() {

    }

    public fun goBack(event:Event) {
        Event.fireEvent(event.getTarget(), NavigateToCalendarEventList())
    }
}