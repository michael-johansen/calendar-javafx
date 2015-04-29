package no.ciber.calendar.repositories

import com.fasterxml.jackson.databind.ObjectMapper
import com.mashape.unirest.http.Unirest
import no.ciber.calendar.Settings
import no.ciber.calendar.model.Location
import no.ciber.calendar.model.User
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object UserRepository : RestRepository() {
    fun list(): List<User> {
        return get("${Settings.eventServiceUrl}/users", javaClass<Array<User>>()).toList()
    }

    fun add(user: User) {
        post("${Settings.eventServiceUrl}/users", user)
    }
}