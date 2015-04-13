package no.ciber.calendar

import javafx.beans.property.ObjectProperty
import javafx.beans.property.Property
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.concurrent.Service
import javafx.concurrent.Task
import no.ciber.calendar.model.CalendarEvent
import no.ciber.calendar.model.SearchMode
import org.slf4j.Logger
import org.slf4j.LoggerFactory


object FetchEventListService : Service<ObservableList<CalendarEvent>>() {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)
    public val searchModeProperty: ObjectProperty<SearchMode> = SimpleObjectProperty()

    override fun createTask(): Task<ObservableList<CalendarEvent>> {
        logger.info("Creating new task")
        return FetchEventListServiceTask(searchModeProperty.get())
    }

    class FetchEventListServiceTask(val searchMode: SearchMode) : Task<ObservableList<CalendarEvent>>() {
        private val logger: Logger = LoggerFactory.getLogger(javaClass)

        override fun call(): ObservableList<CalendarEvent>? {
            try {
                return getEvents()
            } catch(e: Exception) {
                logger.warn("Could not fetch events:", e)
                throw e
            }
        }

        private fun getEvents(): ObservableList<CalendarEvent> {
            val events = CalendarEventRestRepository.list(searchMode)
            return FXCollections.observableArrayList<CalendarEvent>(events)
        }
    }
}