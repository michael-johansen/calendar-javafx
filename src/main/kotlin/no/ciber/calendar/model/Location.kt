package no.ciber.calendar.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleStringProperty

JsonIgnoreProperties(*arrayOf(
        "idProperty",
        "descriptionProperty",
        "locationProperty",
        "latitudeProperty",
        "longitudeProperty"
))
class Location() {
    val idProperty: SimpleStringProperty = SimpleStringProperty()
    val locationProperty: SimpleStringProperty = SimpleStringProperty()
    val descriptionProperty: SimpleStringProperty = SimpleStringProperty()
    val latitudeProperty: SimpleDoubleProperty = SimpleDoubleProperty()
    val longitudeProperty: SimpleDoubleProperty = SimpleDoubleProperty()

    var id: String?
        get() = idProperty.get()
        set(value) = idProperty.set(value)
    var location: String?
        get() = locationProperty.get()
        set(value) = locationProperty.set(value)
    var description: String?
        get() = descriptionProperty.get()
        set(value) = descriptionProperty.set(value)
    var latitude: Double?
        get() = latitudeProperty.get()
        set(value) = latitudeProperty.set(value?:0.0)
    var longitude: Double?
        get() = longitudeProperty.get()
        set(value) = longitudeProperty.set(value?:0.0)

    override fun equals(other: Any?): Boolean {
        return if (other === this) true
        else if (other is Location) id?.equals(other.id) ?: false
        else false
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "$id - $location"
    }
}