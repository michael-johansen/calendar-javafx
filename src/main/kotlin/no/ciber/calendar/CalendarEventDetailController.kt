package no.ciber.calendar

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.mashape.unirest.http.Unirest
import javafx.application.Platform
import javafx.beans.property.ReadOnlyObjectProperty
import javafx.collections.FXCollections
import javafx.event
import javafx.event.ActionEvent
import javafx.event.Event
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.util.StringConverter
import javafx.util.converter.LocalTimeStringConverter
import java.net.URL
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalQueries
import java.util.ArrayList
import java.util.ResourceBundle

class CalendarEventDetailController(val event: CalendarEvent) : Initializable {
    FXML var name: TextField? = null
    FXML var description: TextArea? = null
    FXML var startDate: DatePicker? = null
    FXML var startTime: TextField? = null
    FXML var endDate: DatePicker? = null
    FXML var endTime: TextField? = null
    FXML var locationTextField: TextField? = null


    override fun initialize(location: URL?, resources: ResourceBundle?) {
        name!!.textProperty().bindBidirectional(event.nameProperty)
        description!!.textProperty().bindBidirectional(event.descriptionProperty)
        startDate!!.valueProperty().bindBidirectional(event.startDateProperty)
        endDate!!.valueProperty().bindBidirectional(event.endDateProperty)
        locationTextField!!.textProperty().bindBidirectional(event.locationProperty)

        val startFormatter = TextFormatter(LocalTimeStringConverter())
        val endFormatter = TextFormatter(LocalTimeStringConverter())
        startTime!!.setTextFormatter(startFormatter)
        endTime!!.setTextFormatter(endFormatter)

        startFormatter.valueProperty().bindBidirectional(event.startTimeProperty)
        endFormatter.valueProperty().bindBidirectional(event.endTimeProperty)
    }

    public fun signUp() {

    }

    public fun save(){
        Platform.runLater {
            val mapper = ObjectMapper()
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            val jsonString = mapper.writeValueAsString(event)
            println("Posting:\n $jsonString")

            val post = Unirest.post(Settings.eventServiceUrl)
            post.header("Content-Type", "application/json")
            post.body(jsonString)
            val jsonStringResponse = post.asString()

            println("Response:")
            println("${jsonStringResponse.getStatus()} - ${jsonStringResponse.getStatusText()}")

            val jsonNode: JsonNode = mapper.readTree(jsonStringResponse.getBody())
            println(mapper.writeValueAsString(jsonNode))
        }

    }

    public fun goBack(event: Event) {
        Event.fireEvent(event.getTarget(), NavigateToCalendarEventList())
    }
}