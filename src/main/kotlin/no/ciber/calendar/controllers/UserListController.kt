package no.ciber.calendar.controllers

import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleBooleanProperty
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.event.Event
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.ListView
import javafx.scene.control.SelectionMode
import javafx.scene.control.cell.CheckBoxListCell
import javafx.util.StringConverter
import no.ciber.calendar.NavigateToCalendarEventDetails
import no.ciber.calendar.NavigateToCalendarEventList
import no.ciber.calendar.NavigateToCreateUser
import no.ciber.calendar.model.CalendarEvent
import no.ciber.calendar.model.User
import no.ciber.calendar.repositories.UserRepository
import org.slf4j.LoggerFactory
import java.net.URL
import java.util.ResourceBundle

/**
 * This software is written by Michael on 29/04/2015.
 */
class UserListController(val calendarEvent: CalendarEvent) : Initializable {
    private val logger = LoggerFactory.getLogger(javaClass)
    FXML var userListView: ListView<User>? = null
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        logger.info("Initializing $javaClass")

        userListView!!.setCellFactory(CheckBoxListCell.forListView({ it.selectedProperty }, object : StringConverter<User>() {
            override fun toString(user: User?): String? {
                return "${user?.firstname} ${user?.lastname}"
            }

            override fun fromString(string: String?): User? {
                throw UnsupportedOperationException()
            }

        }))
        userListView!!.setItems(FXCollections.observableArrayList(UserRepository.list()))
        userListView!!.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE)

        Bindings.bindContent(calendarEvent.usersProperty, userListView!!.getSelectionModel().getSelectedItems())
    }

    fun goBack(event: ActionEvent) {
        Event.fireEvent(event.getTarget(), NavigateToCalendarEventDetails(calendarEvent))
    }

    fun createUser(event: ActionEvent) {
        Event.fireEvent(event.getTarget(), NavigateToCreateUser(calendarEvent))
    }
}
