package no.ciber.calendar

import javafx.beans.property.ReadOnlyObjectProperty
import javafx.collections.FXCollections
import javafx.event
import javafx.event.ActionEvent
import javafx.event.Event
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import java.net.URL
import java.time.LocalTime
import java.util.ArrayList
import java.util.ResourceBundle

class CalendarEventDetailController(val event: CalendarEvent) : Initializable {
    FXML var name: TextField? = null
    FXML var description: TextArea? = null
    FXML var startDate: DatePicker? = null
    FXML var startTime: ChoiceBox<LocalTime>? = null
    FXML var endDate: DatePicker? = null
    FXML var endTime: ChoiceBox<LocalTime>? = null


    override fun initialize(location: URL?, resources: ResourceBundle?) {
        name!!.textProperty().bindBidirectional(event.nameProperty)
        description!!.textProperty().bindBidirectional(event.descriptionProperty)
        startDate!!.valueProperty().bindBidirectional(event.startDateProperty)
        endDate!!.valueProperty().bindBidirectional(event.endDateProperty)

        val list = ArrayList<LocalTime>()
        for (hours in 0..23){
            for ( minute in 0..59 step 30){
                list.add(LocalTime.of(hours, minute))
            }
        }
        startTime!!.setItems(FXCollections.observableList(list))
        endTime!!.setItems(FXCollections.observableList(list))
        startTime!!.setValue(event.startTimeProperty.get())
        endTime!!.setValue(event.endTimeProperty.get())

        event.startTimeProperty.bind(startTime!!.getSelectionModel().selectedItemProperty()!!)
        event.endTimeProperty.bind(endTime!!.getSelectionModel().selectedItemProperty()!!)
    }

    public fun signUp() {

    }

    public fun save(){
        println(event)
    }

    public fun goBack(event: Event) {
        Event.fireEvent(event.getTarget(), NavigateToCalendarEventList())
    }
}