package no.ciber.calendar.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javafx.beans.property.SimpleStringProperty


JsonIgnoreProperties(array(
        "idProperty",
        "emailProperty",
        "firstNameProperty",
        "lastNameProperty"
))
class User {
    val idProperty = SimpleStringProperty()
    val emailProperty = SimpleStringProperty()
    val firstnameProperty = SimpleStringProperty()
    val lastnameProperty = SimpleStringProperty()

    var id: String
        get() = idProperty.get()
        set(value) = idProperty.set(value)
    var email: String
        get() = emailProperty.get()
        set(value) = emailProperty.set(value)
    var firstname: String
        get() = firstnameProperty.get()
        set(value) = firstnameProperty.set(value)
    var lastname: String
        get() = lastnameProperty.get()
        set(value) = lastnameProperty.set(value)
}