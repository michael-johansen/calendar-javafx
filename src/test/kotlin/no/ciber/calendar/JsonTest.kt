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
"createdDate": 1430299393209,
"startDate": 1430297862045,
"endDate": 1430297862045,
"name": "Partytime",
"description": "The greatest party in the history of mankind!",
"users": [
{
"id": 8,
"email": "mm@ciber.com",
"firstname": "Mihaela",
"lastname": "MM"
}
],
"location": {
"id": 7,
"description": "heyhoo",
"location": "roma",
"latitude": 62.73379,
"longitude": 17.14795
}
}"""

    Test fun canParseJson(){
        val mapper = ObjectMapper()
        val event = mapper.readValue(json, javaClass<CalendarEvent>())
        Assert.assertThat(event.name, CoreMatchers.`is`("Partytime"))


    }

}
