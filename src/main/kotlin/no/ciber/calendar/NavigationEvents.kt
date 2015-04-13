package no.ciber.calendar

import javafx.event.Event
import no.ciber.calendar.model.CalendarEvent

open class NavigateToLocationEvent(val location:String): Event(Event.ANY)

class NavigateToCalendarEventDetails(val calendarEvent: CalendarEvent) :
        NavigateToLocationEvent("details/event-details.fxml")

class NavigateToCalendarEventList :
        NavigateToLocationEvent("list/event-list.fxml")