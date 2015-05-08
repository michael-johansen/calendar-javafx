package no.ciber.calendar.repositories

import no.ciber.calendar.Settings
import no.ciber.calendar.model.User

object UserRepository : RestRepository() {
    fun list(): List<User> {
        return get("${Settings.eventServiceUrl}/users", javaClass<Array<User>>()).toList()
    }

    fun add(user: User) {
        post("${Settings.eventServiceUrl}/users", user)
    }
}