package no.ciber.calendar.controllers

import javafx.application.Platform
import javafx.beans.Observable
import javafx.event.ActionEvent
import javafx.event.Event
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.input.MouseEvent
import javafx.scene.web.WebView
import javafx.util.converter.NumberStringConverter
import netscape.javascript.JSObject
import no.ciber.calendar.NavigateToCalendarEventDetails
import no.ciber.calendar.model.CalendarEvent
import no.ciber.calendar.model.Location
import no.ciber.calendar.repositories.LocationRepository
import org.slf4j.LoggerFactory
import java.net.URL
import java.util.ResourceBundle
import java.util.Timer
import java.util.TimerTask

/**
 * User: Michael Johansen
 * Date: 30.04.2015
 * Time: 14:33
 */
public class CreateLocationController(val event: CalendarEvent, val location: Location) : Initializable {
    private val logger = LoggerFactory.getLogger(javaClass)
    FXML private var locationTextField: TextField? = null
    FXML private var descriptionTextField: TextField? = null
    FXML private var mapWebView: WebView? = null
    FXML private var latitudeLabel: Label? = null
    FXML private var longitudeLabel: Label? = null

    override fun initialize(location: URL, resources: ResourceBundle) {
        val webEngine = mapWebView!!.getEngine()
        webEngine.getLoadWorker().exceptionProperty().addListener({ it: Observable -> logger.info("${it}") })
        webEngine.load(javaClass.getResource("../html/map.html").toExternalForm())
        locationTextField!!.textProperty().bindBidirectional(this.location.locationProperty)
        descriptionTextField!!.textProperty().bindBidirectional(this.location.descriptionProperty)
        latitudeLabel!!.textProperty().bindBidirectional(this.location.latitudeProperty, NumberStringConverter())
        longitudeLabel!!.textProperty().bindBidirectional(this.location.longitudeProperty, NumberStringConverter())
    }

    fun goBack(actionEvent: ActionEvent) {
        Event.fireEvent(actionEvent.getTarget(), NavigateToCalendarEventDetails(this.event))
    }

    fun createLocation(actionEvent: ActionEvent) {
        LocationRepository.create(location);
        Event.fireEvent(actionEvent.getTarget(), NavigateToCalendarEventDetails(this.event))
    }

    fun updateCoordinates(mouseEvent: MouseEvent){
        Platform.runLater({
            val js = mapWebView!!.getEngine().executeScript("document.getCoordinates()")
            when(js){
                is JSObject ->      {
                    location.longitude = java.lang.Double.parseDouble(js.getMember("longitude").toString())
                    location.latitude = java.lang.Double.parseDouble(js.getMember("latitude").toString())
                    location.location = js.getMember("location").toString()
                    logger.info("Location: (${location.longitude}, ${location.latitude})/${location.location}")
                }
            }
        })
    }
}
