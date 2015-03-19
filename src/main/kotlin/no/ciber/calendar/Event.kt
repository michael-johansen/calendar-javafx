package no.ciber.calendar

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

[JsonIgnoreProperties(array("class"))]
class Event(
        val name: String? = "",
        val description: String?= "",
        val id: String?= "",
        val createdDate: String?= "",
        val eventDate: String?= "",
        val users: Array<String> = array()) {
}