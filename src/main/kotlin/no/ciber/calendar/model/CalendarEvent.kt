package no.ciber.calendar.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.ObservableList
import no.ciber.calendar.Settings
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset

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
class CalendarEvent {
    val nameProperty = SimpleStringProperty()
    val descriptionProperty = SimpleStringProperty()
    val idProperty = SimpleStringProperty()
    val createdDateProperty = SimpleObjectProperty<LocalDateTime>()
    val startDateTimeProperty = SimpleObjectProperty<LocalDateTime>()
    val endDateTimeProperty = SimpleObjectProperty<LocalDateTime>()
    val startDateProperty = SimpleObjectProperty<LocalDate>()
    val endDateProperty = SimpleObjectProperty<LocalDate>()
    val startTimeProperty = SimpleObjectProperty<LocalTime>()
    val endTimeProperty = SimpleObjectProperty<LocalTime>()
    val locationProperty = SimpleObjectProperty<Location>()
    val usersProperty: ObservableList<User> = SimpleListProperty()


    public var name: String?
        get() = nameProperty.getValue()
        set(value) = nameProperty.set(value)
    var description: String?
        get() = descriptionProperty.getValue()
        set(value) = descriptionProperty.set(value)
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

    var location: Location?
        get() = locationProperty.getValue()
        set(value) = locationProperty.set(value)
    val users: List<User>
        get() = usersProperty.toList()

    constructor() {
        /** Update DateTime when date or time changes */
        startDateProperty.addListener({(observable, oldValue, newValue) -> startDateTimeProperty.set(startDateTimeProperty.get().with(newValue)) })
        startTimeProperty.addListener({(observable, oldValue, newValue) -> startDateTimeProperty.set(startDateTimeProperty.get().with(newValue)) })
        endDateProperty.addListener({(observable, oldValue, newValue) -> endDateTimeProperty.set(endDateTimeProperty.get().with(newValue)) })
        endTimeProperty.addListener({(observable, oldValue, newValue) -> endDateTimeProperty.set(endDateTimeProperty.get().with(newValue)) })
    }

    override fun toString(): String {
        return """Event[$name: ($startDate-$endDate) ]""""
    }
}

