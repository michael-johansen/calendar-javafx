package no.ciber.calendar.controllers

import javafx.beans.Observable
import javafx.concurrent.Worker
import javafx.event.ActionEvent
import javafx.event.Event
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import javafx.scene.control.TextField
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

    override fun initialize(urlLocation: URL, resources: ResourceBundle) {
        initWebView()
        locationTextField!!.textProperty().bindBidirectional(this.location.locationProperty)
        descriptionTextField!!.textProperty().bindBidirectional(this.location.descriptionProperty)
        latitudeLabel!!.textProperty().bindBidirectional(this.location.latitudeProperty, NumberStringConverter())
        longitudeLabel!!.textProperty().bindBidirectional(this.location.longitudeProperty, NumberStringConverter())
    }

    private fun initWebView() {
        val webEngine = mapWebView!!.getEngine()
        webEngine.getLoadWorker().exceptionProperty().addListener({ it: Observable -> logger.info("${it}") })
        webEngine.setOnError { it.getException().printStackTrace() }
        webEngine.getLoadWorker().stateProperty().addListener { observableValue, from, to ->
            if (to.equals(Worker.State.SUCCEEDED)) {
                val window = webEngine.executeScript("window") as JSObject
                window.setMember("fxLocation", LocationBridge(location))
                window.setMember("javaLogger", LogBridge(logger))
                webEngine.executeScript("console.log = function(message){javaLogger.log(message);};")
            }
        }
        webEngine.load(javaClass.getResource("../html/map.html").toExternalForm())
    }

    fun goBack(actionEvent: ActionEvent) {
        Event.fireEvent(actionEvent.getTarget(), NavigateToCalendarEventDetails(this.event))
    }

    fun createLocation(actionEvent: ActionEvent) {
        LocationRepository.create(location);
        Event.fireEvent(actionEvent.getTarget(), NavigateToCalendarEventDetails(this.event))
    }
}
