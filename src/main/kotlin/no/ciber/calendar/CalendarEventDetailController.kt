package no.ciber.calendar

import javafx.event
import javafx.event.ActionEvent
import javafx.event.Event
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import javafx.scene.control.TextArea
import java.net.URL
import java.util.ResourceBundle

class CalendarEventDetailController(val event: CalendarEvent) : Initializable {
    FXML var name: Label? = null
    FXML var description: TextArea? = null
    FXML var startDate: Label? = null
    FXML var endDate: Label? = null

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        name!!.textProperty().bind(event.nameProperty)
        description!!.textProperty().bind(event.descriptionProperty)
        startDate!!.textProperty().bind(event.startDateProperty)
        endDate!!.textProperty().bind(event.endDateProperty)

    }

    public fun signUp() {

    }

    public fun goBack(event: Event) {
        Event.fireEvent(event.getTarget(), NavigateToCalendarEventList())
    }
}