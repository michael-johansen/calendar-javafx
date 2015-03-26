package no.ciber.calendar

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.value.ObservableValue
import javafx.collections.ObservableList
import java.util.ArrayList

JsonIgnoreProperties(array(
        "class",
        "nameProperty",
        "descriptionProperty",
        "idProperty",
        "createdDateProperty",
        "startDateProperty",
        "endDateProperty",
        "usersProperty"
))
class CalendarEvent() {
    val nameProperty = SimpleStringProperty()
    val descriptionProperty = SimpleStringProperty()
    val locationProperty = SimpleStringProperty()
    val idProperty = SimpleStringProperty()
    val createdDateProperty = SimpleStringProperty()
    val startDateProperty = SimpleStringProperty()
    val endDateProperty = SimpleStringProperty()
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
        get() = createdDateProperty.getValue()
        set(value) = createdDateProperty.set(value)
    var startDate: String?
        get() = startDateProperty.getValue()
        set(value) = startDateProperty.set(value)
    var endDate: String?
        get() = endDateProperty.getValue()
        set(value) = endDateProperty.set(value)
    val users: List<String>
        get() = usersProperty.toList()

}