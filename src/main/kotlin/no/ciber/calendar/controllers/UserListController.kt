package no.ciber.calendar.controllers

import javafx.beans.binding.Bindings
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.event.Event
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.ListView
import javafx.scene.control.SelectionMode
import no.ciber.calendar.NavigateToCalendarEventDetails
import no.ciber.calendar.NavigateToCreateUser
import no.ciber.calendar.components.SelectableImageListCell
import no.ciber.calendar.model.CalendarEvent
import no.ciber.calendar.model.User
import no.ciber.calendar.repositories.UserRepository
import no.ciber.util.liveFiltered
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

        userListView!!.setCellFactory {
            it: ListView<User> ->
            SelectableImageListCell<User>({
                Bindings.concat(it.gravatarUrlProperty, "?s=64")
            }, {
                it.selectedProperty
            }, {
                Bindings.concat(it.firstnameProperty," ", it.lastnameProperty, " (", it.emailProperty, ")")
            })
        }
        userListView!!.setItems(FXCollections.observableArrayList(UserRepository.list()))
        userListView!!.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE)

        val all = FXCollections.observableList(UserRepository.list())
        all.filter { it in calendarEvent.users }.forEach { it.selected = true }
        userListView!!.setItems(all)

        Bindings.bindContent(calendarEvent.users, all.liveFiltered({ it.selectedProperty }, { it.selected }))
    }

    fun goBack(event: ActionEvent) {
        Event.fireEvent(event.getTarget(), NavigateToCalendarEventDetails(calendarEvent))
    }

    fun createUser(event: ActionEvent) {
        Event.fireEvent(event.getTarget(), NavigateToCreateUser(calendarEvent))
    }
}
