package no.ciber.calendar

import com.fasterxml.jackson.databind.ObjectMapper
import no.ciber.calendar.model.CalendarEvent
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Test

class JsonTest{
    val json =
"""{
    "id": 1,
    "createdDate": "2015-03-25T15:52:26Z",
    "startDate": "2015-03-25T15:47:30Z",
    "endDate": "2015-03-26T17:47:00Z",
    "name": "Partytime",
    "description": "The greatest party in the history of mankind!",
    "users": [
        {
            "id": 1,
            "email": "a@b.c",
            "firstname": "Tester",
            "lastname": "Abcd"
        }
    ],
    "location": {
        "id": 1,
        "description": "Fotballkamp",
        "location": "Molde",
        "latitude": 62.73379,
        "longitude": 7.14795
    }
}
"""

    Test fun canParseJson(){
        val mapper = ObjectMapper()
        val event = mapper.readValue(json, javaClass<CalendarEvent>())
        Assert.assertThat(event.name, CoreMatchers.`is`("Partytime"))


    }

}
