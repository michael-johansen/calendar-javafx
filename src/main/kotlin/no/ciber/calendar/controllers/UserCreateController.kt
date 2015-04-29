package no.ciber.calendar.controllers

import javafx.event.ActionEvent
import javafx.event.Event
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.TextField
import no.ciber.calendar.NavigateToAddUsersToEvent
import no.ciber.calendar.NavigateToCalendarEventDetails
import no.ciber.calendar.model.CalendarEvent
import no.ciber.calendar.model.User
import no.ciber.calendar.repositories.UserRepository
import java.net.URL
import java.util.ResourceBundle

/**
 * This software is written by Michael on 29/04/2015.
 */
class UserCreateController(val calendarEvent: CalendarEvent, val user: User) : Initializable {
    FXML var firstnameTextField: TextField? = null
    FXML var lastnameTextField: TextField? = null
    FXML var emailTextField: TextField? = null

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        firstnameTextField!!.textProperty().bindBidirectional(user.firstnameProperty)
        lastnameTextField!!.textProperty().bindBidirectional(user.lastnameProperty)
        emailTextField!!.textProperty().bindBidirectional(user.emailProperty)
    }


    public fun goBack(actionEvent: ActionEvent) {
        Event.fireEvent(actionEvent.getTarget(), NavigateToAddUsersToEvent(calendarEvent))
    }

    public fun createUser(actionEvent: ActionEvent) {
        UserRepository.add(user)
    }
}
