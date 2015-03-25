package no.ciber.calendar

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.value.ObservableValue
import javafx.collections.ObservableList
import java.util.ArrayList

[JsonIgnoreProperties(array(
        "class",
        "nameProperty",
        "descriptionProperty",
        "idProperty",
        "createdDateProperty",
        "startDateProperty",
        "endDateProperty",
        "usersProperty"
))]
class CalendarEvent() {
    val nameProperty: ObservableValue<String> = SimpleStringProperty()
    val descriptionProperty: ObservableValue<String> = SimpleStringProperty()
    val locationProperty: ObservableValue<String> = SimpleStringProperty()
    val idProperty: ObservableValue<String> = SimpleStringProperty()
    val createdDateProperty: ObservableValue<String> = SimpleStringProperty()
    val startDateProperty: ObservableValue<String> = SimpleStringProperty()
    val endDateProperty: ObservableValue<String> = SimpleStringProperty()
    val usersProperty: ObservableList<String> = SimpleListProperty()


    var name: String? = null
        get() = nameProperty.getValue()
        set(value) = nameProperty!!.
    var description: String? = null
    var location: String? = null
    var id: String? = null
    var createdDate: String? = null
    var startDate: String? = null
    var endDate: String? = null
    var users: List<String> = ArrayList()


    public fun textProperty(): ObservableValue<String> {
        val simpleStringProperty = SimpleStringProperty()
        simpleStringProperty.setValue(name)
        return simpleStringProperty
    }


}