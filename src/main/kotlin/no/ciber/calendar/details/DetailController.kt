package no.ciber.calendar.details

import javafx.beans.binding.BooleanBinding
import javafx.collections.FXCollections
import javafx.concurrent.Task
import javafx.event.Event
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.util.StringConverter
import javafx.util.converter.LocalTimeStringConverter
import no.ciber.calendar.repositories.CalendarEventRestRepository
import no.ciber.calendar.NavigateToCalendarEventList
import no.ciber.calendar.model.CalendarEvent
import no.ciber.calendar.model.Location
import no.ciber.calendar.repositories.LocationRepository
import java.net.URL
import java.util.ResourceBundle

class DetailController(val event: CalendarEvent) : Initializable {
    FXML var name: TextField? = null
    FXML var description: TextArea? = null
    FXML var startDate: DatePicker? = null
    FXML var startTime: TextField? = null
    FXML var endDate: DatePicker? = null
    FXML var endTime: TextField? = null
    FXML var locationChoiceBox: ChoiceBox<Location>? = null
    FXML var saveButton: Button? = null
    FXML var deleteButton: Button? = null

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        name!!.textProperty().bindBidirectional(event.nameProperty)
        description!!.textProperty().bindBidirectional(event.descriptionProperty)
        startDate!!.valueProperty().bindBidirectional(event.startDateProperty)
        endDate!!.valueProperty().bindBidirectional(event.endDateProperty)

        locationChoiceBox!!.setConverter(LocationConverter)l
        locationChoiceBox!!.setItems(FXCollections.observableArrayList(LocationRepository.getAllLocations()))
        locationChoiceBox!!.valueProperty().bindBidirectional(event.locationProperty)
        locationChoiceBox!!.getSelectionModel().select(event.location)

        val startFormatter = TextFormatter(LocalTimeStringConverter())
        val endFormatter = TextFormatter(LocalTimeStringConverter())
        startTime!!.setTextFormatter(startFormatter)
        endTime!!.setTextFormatter(endFormatter)

        startFormatter.valueProperty().bindBidirectional(event.startTimeProperty)
        endFormatter.valueProperty().bindBidirectional(event.endTimeProperty)

        val isNotValid: BooleanBinding = event.nameProperty.isEmpty()
                .or(event.descriptionProperty.isEmpty())
                .or(event.locationProperty.isNull())
                // Need to find a better way of comparing these.
                .or(event.endDateTimeProperty.asString().lessThan(event.startDateTimeProperty.asString()))



        saveButton!!.disableProperty().bind(isNotValid)
    }

    public fun signUp() {

    }

    public fun save() {
        val task = object : Task<Unit>() {
            override fun call() {
                CalendarEventRestRepository.save(event)
                Event.fireEvent(saveButton, NavigateToCalendarEventList())
            }

        }
        Thread(task).start()
    }

    public fun delete() {
        val task = object : Task<Unit>() {
            override fun call() {
                CalendarEventRestRepository.delete(event)
                Event.fireEvent(deleteButton, NavigateToCalendarEventList())
            }

        }
        Thread(task).start()
    }

    public fun goBack(event: Event) {
        Event.fireEvent(event.getTarget(), NavigateToCalendarEventList())
    }
}