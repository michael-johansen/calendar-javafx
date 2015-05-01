package no.ciber.calendar

import javafx.event.Event
import no.ciber.calendar.model.CalendarEvent
import no.ciber.calendar.model.Location
import no.ciber.calendar.model.SearchMode
import no.ciber.calendar.model.User
import java.util.Locale

open class NavigateToLocationEvent(val layoutLocation: String) : Event(Event.ANY)
class NavigateToCalendarEventDetails(val calendarEvent: CalendarEvent) : NavigateToLocationEvent("layouts/event-details.fxml")
class NavigateToCalendarEventList(val searchMode: SearchMode = SearchMode.All) : NavigateToLocationEvent("layouts/event-list.fxml")
class NavigateToAddUsersToEvent(val calendarEvent: CalendarEvent) : NavigateToLocationEvent("layouts/user-list.fxml")
class NavigateToCreateUser(val calendarEvent: CalendarEvent, val user: User = User()) : NavigateToLocationEvent("layouts/user-create.fxml")
class NavigateToCreateLocation(val calendarEvent: CalendarEvent, val location: Location = Location()) : NavigateToLocationEvent("layouts/location-create.fxml")

class ChangeLocale(val locale: Locale) : Event(Event.ANY)