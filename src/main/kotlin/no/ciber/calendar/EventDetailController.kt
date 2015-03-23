package no.ciber.calendar

import javafx.event
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.Label

class EventDetailController(application: CalendarApplication) : BaseController(application) {
    [FXML]
    private val eventLabel = Label();

    public fun signUp() {

    }

    public fun goBack() {
        gotoScene("event-list.fxml")
    }
}