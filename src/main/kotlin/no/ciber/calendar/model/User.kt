package no.ciber.calendar.model


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty

JsonIgnoreProperties(array(
        "selected",
        "idProperty",
        "emailProperty",
        "firstnameProperty",
        "lastnameProperty",
        "selectedProperty"
))
class User {
    val idProperty = SimpleStringProperty()
    val emailProperty = SimpleStringProperty()
    val firstnameProperty = SimpleStringProperty()
    val lastnameProperty = SimpleStringProperty()
    val selectedProperty = SimpleBooleanProperty()

    var id: String?
        get() = idProperty.get()
        set(value) = idProperty.set(value)
    var email: String?
        get() = emailProperty.get()
        set(value) = emailProperty.set(value)
    var firstname: String?
        get() = firstnameProperty.get()
        set(value) = firstnameProperty.set(value)
    var lastname: String?
        get() = lastnameProperty.get()
        set(value) = lastnameProperty.set(value)
    var selected: Boolean
        get() = selectedProperty.get()
        set(value) = selectedProperty.set(value)

    override fun equals(other: Any?): Boolean {
        return if (other === this) true
        else if (other is User) id?.equals(other.id) ?: false
        else false
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}
