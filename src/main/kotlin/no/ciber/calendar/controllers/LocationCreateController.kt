package no.ciber.calendar.controllers

import javafx.beans.Observable
import javafx.event.ActionEvent
import javafx.event.Event
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.TextField
import javafx.scene.web.WebView
import no.ciber.calendar.NavigateToCalendarEventDetails
import no.ciber.calendar.model.CalendarEvent
import no.ciber.calendar.model.Location
import org.slf4j.LoggerFactory
import java.net.URL
import java.util.ResourceBundle

/**
 * User: Michael Johansen
 * Date: 30.04.2015
 * Time: 14:33
 */
public class LocationCreateController(val event: CalendarEvent, val location: Location) : Initializable {
    private val logger = LoggerFactory.getLogger(javaClass)
    FXML private var locationTextField: TextField? = null
    FXML private var descriptionTextField: TextField? = null
    FXML private var mapWebView: WebView? = null

    override fun initialize(location: URL, resources: ResourceBundle) {
        val webEngine = mapWebView!!.getEngine()
        webEngine.getLoadWorker().exceptionProperty().addListener({ it: Observable -> logger.info("${it}") })
        webEngine.load(javaClass.getResource("../html/map.html").toExternalForm())
    }

    public fun goBack(actionEvent: ActionEvent) {
        Event.fireEvent(actionEvent.getTarget(), NavigateToCalendarEventDetails(this.event))
    }

    public fun createLocation(actionEvent: ActionEvent) {
        Event.fireEvent(actionEvent.getTarget(), NavigateToCalendarEventDetails(this.event))
    }
}
