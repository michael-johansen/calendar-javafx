package no.ciber.calendar.controllers

import no.ciber.calendar.model.Location
import org.slf4j.Logger

class LocationBridge(val location: Location) {
    public fun setLocation(locationText: String): Unit {
        location.location = locationText
    }

    public fun setLatitude(latitude: Double): Unit {
        location.latitude = latitude.toString().toDouble()
    }

    public fun setLongitude(longitude: Double): Unit {
        location.longitude = longitude.toString().toDouble()
    }

}

