package no.ciber.calendar

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ObjectWriter
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.value
import javafx.beans.value.ObservableValue
import javafx.collections.ObservableList
import java.io.PrintWriter
import java.io.StringWriter
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField
import java.time.temporal.TemporalAdjusters
import java.time.temporal.TemporalField
import java.util.ArrayList

JsonIgnoreProperties(array(
        "nameProperty",
        "descriptionProperty",
        "idProperty",
        "createdDateProperty",
        "startDateTimeProperty",
        "endDateTimeProperty",
        "startDateProperty",
        "endDateProperty",
        "startTimeProperty",
        "endTimeProperty",
        "locationProperty",
        "usersProperty"
))
JsonDeserialize()
class CalendarEvent {
    val nameProperty = SimpleStringProperty()
    val descriptionProperty = SimpleStringProperty()
    val locationProperty = SimpleStringProperty()
    val idProperty = SimpleStringProperty()
    val createdDateProperty = SimpleObjectProperty<LocalDateTime>()
    val startDateTimeProperty = SimpleObjectProperty<LocalDateTime>()
    val endDateTimeProperty = SimpleObjectProperty<LocalDateTime>()
    val startDateProperty = SimpleObjectProperty<LocalDate>()
    val endDateProperty = SimpleObjectProperty<LocalDate>()
    val startTimeProperty = SimpleObjectProperty<LocalTime>()
    val endTimeProperty = SimpleObjectProperty<LocalTime>()
    val usersProperty: ObservableList<String> = SimpleListProperty()


    var name: String?
        get() = nameProperty.getValue()
        set(value) = nameProperty.set(value)
    var description: String?
        get() = descriptionProperty.getValue()
        set(value) = descriptionProperty.set(value)
    var location: String?
        get() = locationProperty.getValue()
        set(value) = locationProperty.set(value)
    var id: String?
        get() = idProperty.getValue()
        set(value) = idProperty.set(value)
    var createdDate: String?
        get() = createdDateProperty.getValue().atOffset(ZoneOffset.ofHours(0)).format(Settings.eventDateFormat())
        set(value) = createdDateProperty.setValue(LocalDateTime.parse(value, Settings.eventDateFormat()))
    var startDate: String?
        get() = startDateTimeProperty.getValue().atOffset(ZoneOffset.ofHours(0)).format(Settings.eventDateFormat())
        set(value) {
            startDateTimeProperty.setValue(LocalDateTime.parse(value, Settings.eventDateFormat()))
            startDateProperty.setValue(startDateTimeProperty.get().toLocalDate())
            startTimeProperty.setValue(startDateTimeProperty.get().toLocalTime())
        }
    var endDate: String?
        get() = endDateTimeProperty.getValue().atOffset(ZoneOffset.ofHours(0)).format(Settings.eventDateFormat())
        set(value) {
            endDateTimeProperty.setValue(LocalDateTime.parse(value, Settings.eventDateFormat()))
            endDateProperty.setValue(endDateTimeProperty.get().toLocalDate())
            endTimeProperty.setValue(endDateTimeProperty.get().toLocalTime())
        }
    val users: List<String>
         get() = usersProperty.toList()

    JsonProperty(value="class")
    public fun getClazz():String = "no.ciber.service.Event"
    public fun setClazz(clazz:String){}

    constructor(){
        /** Update DateTime when date or time changes */
        startDateProperty.addListener({(observable, oldValue, newValue)-> startDateTimeProperty.set(startDateTimeProperty.get().with(newValue))})
        startTimeProperty.addListener({(observable, oldValue, newValue)-> startDateTimeProperty.set(startDateTimeProperty.get().with(newValue))})
        endDateProperty.addListener({(observable, oldValue, newValue)-> endDateTimeProperty.set(endDateTimeProperty.get().with(newValue))})
        endTimeProperty.addListener({(observable, oldValue, newValue)-> endDateTimeProperty.set(endDateTimeProperty.get().with(newValue))})
    }

    override fun toString(): String {
        val mapper = ObjectMapper()
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        return mapper.writeValueAsString(this)
    }
}