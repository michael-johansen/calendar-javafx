package no.ciber.calendar

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javafx.beans.Observable
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.beans.value.ObservableValue

[JsonIgnoreProperties(array("class", "textProperty"))]
class Event(
        val name: String? = "",
        val description: String? = "",
        val id: String? = "",
        val createdDate: String? = "",
        val eventDate: String? = "",
        val users: Array<String> = array()) {

    public fun textProperty(): ObservableValue<String> {
        val simpleStringProperty = SimpleStringProperty()
        simpleStringProperty.setValue(name)
        return simpleStringProperty
    }
}