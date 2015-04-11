package no.ciber.calendar

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.mashape.unirest.http.HttpResponse
import com.mashape.unirest.http.Unirest
import javafx.application.Platform
import javafx.beans.binding.Bindings
import javafx.beans.binding.BooleanBinding
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
    FXML var saveButton : Button? = null
    FXML var deleteButton : Button? = null

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

        val isNotValid: BooleanBinding = event.nameProperty.isEmpty()
                .or(event.descriptionProperty.isEmpty())
                .or(event.locationProperty.isEmpty())
                // Need to find a better way of comparing these.
                .or(event.endDateTimeProperty.asString().lessThan(event.startDateTimeProperty.asString()))



        saveButton!!.disableProperty().bind(isNotValid)
    }

    public fun signUp() {

    }

    public fun save(){
        Platform.runLater {
            val mapper = ObjectMapper()
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            val jsonString = mapper.writeValueAsString(event)
            println("Posting:\n $jsonString")

            val request = if (event.id == null) Unirest.post(Settings.eventServiceUrl) else Unirest.put("${Settings.eventServiceUrl}/${event.id}");
            request.header("Content-Type", "application/json")
            request.body(jsonString)
            val jsonStringResponse = request.asString()

            printResponse(jsonStringResponse)

            val jsonNode: JsonNode = mapper.readTree(jsonStringResponse.getBody())
            println(mapper.writeValueAsString(jsonNode))
        }

    }

    private fun printResponse(jsonStringResponse: HttpResponse<String>) {
        println("Response:")
        println("${jsonStringResponse.getStatus()} - ${jsonStringResponse.getStatusText()}")
    }

    public fun delete(actionEvent: ActionEvent){
        Platform.runLater {
            val request = Unirest.delete("${Settings.eventServiceUrl}/${event.id}")
            val response = request.asString()
            printResponse(response)
            if(response.getStatus() in 200..299){
                Event.fireEvent(actionEvent.getTarget(), NavigateToCalendarEventList())
            }
        }
    }

    public fun goBack(event: Event) {
        Event.fireEvent(event.getTarget(), NavigateToCalendarEventList())
    }
}