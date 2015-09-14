package no.ciber.calendar.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import no.ciber.calendar.Settings
import java.time.*
import java.util.ArrayList

JsonIgnoreProperties(*arrayOf(
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
    val createdDateProperty = SimpleObjectProperty<Long>()
    val startDateTimeProperty = SimpleObjectProperty<LocalDateTime>()
    val endDateTimeProperty = SimpleObjectProperty<LocalDateTime>()
    val startDateProperty = SimpleObjectProperty<LocalDate>()
    val endDateProperty = SimpleObjectProperty<LocalDate>()
    val startTimeProperty = SimpleObjectProperty<LocalTime>()
    val endTimeProperty = SimpleObjectProperty<LocalTime>()
    val locationProperty = SimpleObjectProperty<Location>()
    val usersProperty = SimpleListProperty<User>(FXCollections.observableArrayList())


    public var name: String?
        get() = nameProperty.getValue()
        set(value) = nameProperty.set(value)
    var description: String?
        get() = descriptionProperty.getValue()
        set(value) = descriptionProperty.set(value)
    var id: String?
        get() = idProperty.getValue()
        set(value) = idProperty.set(value)
    var createdDate: Long?
        get() = createdDateProperty.getValue()
        set(value) = createdDateProperty.set(value)
    var startDate: Long?
        get() = startDateTimeProperty.getValue().toInstant(ZoneOffset.ofHours(0)).toEpochMilli()
        set(value) = setDate(value, startDateTimeProperty, startDateProperty, startTimeProperty)
    var endDate: Long?
        get() = endDateTimeProperty.getValue().toInstant(ZoneOffset.ofHours(0)).toEpochMilli()
        set(value)= setDate(value, endDateTimeProperty, endDateProperty, endTimeProperty)

    var location: Location?
        get() = locationProperty.getValue()
        set(value) = locationProperty.set(value)
    val users: MutableList<User>
        get() = usersProperty.get()

    constructor() {
        /** Update DateTime when date or time changes */
        startDateProperty.addListener({ observable, oldValue, newValue -> startDateTimeProperty.set(startDateTimeProperty.get().with(newValue)) })
        startTimeProperty.addListener({ observable, oldValue, newValue -> startDateTimeProperty.set(startDateTimeProperty.get().with(newValue)) })
        endDateProperty.addListener({ observable, oldValue, newValue -> endDateTimeProperty.set(endDateTimeProperty.get().with(newValue)) })
        endTimeProperty.addListener({ observable, oldValue, newValue -> endDateTimeProperty.set(endDateTimeProperty.get().with(newValue)) })
    }

    override fun toString(): String {
        return """Event[$name: ($startDate-$endDate) ]""""
    }

    fun setDate(
            epochMillis: Long?,
            dateTime: SimpleObjectProperty<LocalDateTime>,
            date: SimpleObjectProperty<LocalDate>,
            time: SimpleObjectProperty<LocalTime>
    ) {
        when (epochMillis) {
            null -> {
                dateTime.set(null)
                date.set(null)
                time.set(null)
            }
            else -> {
                val localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMillis), ZoneId.systemDefault())
                dateTime.setValue(localDateTime)
                date.set(localDateTime.toLocalDate())
                time.set(localDateTime.toLocalTime())
            }
        }
    }
}

