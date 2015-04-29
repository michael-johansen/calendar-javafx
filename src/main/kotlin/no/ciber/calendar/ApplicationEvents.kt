package no.ciber.calendar

import javafx.event.Event
import no.ciber.calendar.model.CalendarEvent
import no.ciber.calendar.model.SearchMode
import java.util.Locale

open class NavigateToLocationEvent(val location: String) : Event(Event.ANY)
class NavigateToCalendarEventDetails(val calendarEvent: CalendarEvent) : NavigateToLocationEvent("layouts/event-details.fxml")
class NavigateToCalendarEventList(val searchMode:SearchMode = SearchMode.All) : NavigateToLocationEvent("layouts/event-list.fxml")

class ChangeLocale(val locale: Locale) : Event(Event.ANY)