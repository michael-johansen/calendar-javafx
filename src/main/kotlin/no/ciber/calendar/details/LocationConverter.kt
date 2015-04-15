package no.ciber.calendar.details

import javafx.util.StringConverter
import no.ciber.calendar.model.Location


object LocationConverter : StringConverter<Location>() {
    override fun fromString(string: String?): Location? {
        throw UnsupportedOperationException()
    }

    override fun toString(location: Location?): String? {
        return location?.location ?: ""
    }

}