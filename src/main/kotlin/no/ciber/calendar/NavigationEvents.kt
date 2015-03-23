package no.ciber.calendar

import javafx.event.Event

open class NavigateToLocationEvent(val location:String): Event(Event.ANY)

class NavigateToCalendarEventDetails(val calendarEvent:CalendarEvent) :
        NavigateToLocationEvent("event-details.fxml")

class NavigateToCalendarEventList :
        NavigateToLocationEvent("event-list.fxml")