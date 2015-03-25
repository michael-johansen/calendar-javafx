package no.ciber.calendar

import javafx.application.Application
import javafx.application.Platform
import javafx.event.Event
import javafx.fxml.FXMLLoader
import javafx.fxml.JavaFXBuilderFactory
import javafx.scene.Group
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.stage.Stage
import org.slf4j.LoggerFactory
import java.net.URL

class CalendarApplication : Application() {
    val logger = LoggerFactory.getLogger(javaClass)
    val scene = Scene(Group(Label("loading")), 400.0, 400.0)

    override fun start(primaryStage: Stage) {
        logger.info("Starting application")
        primaryStage.setScene(scene)
        primaryStage.show()
        primaryStage.addEventHandler(
                Event.ANY,
                { event ->
                    when (event) {
                        is NavigateToCalendarEventDetails -> gotoView(event.location, CalendarEventDetailController(event.calendarEvent))
                        is NavigateToCalendarEventList -> gotoView(event.location)
                    }
                })

        Platform.runLater {
            logger.info("Navigating to event list")
            primaryStage.fireEvent(NavigateToCalendarEventList())
        }
    }

    fun <Controller> gotoView(fxml: String, controller: Any? = null): Controller {
        val loader = FXMLLoader()
        loader.setBuilderFactory(JavaFXBuilderFactory())
        loader.setLocation(getRequiredResource(fxml))
        loader.setController(controller)
        scene.setRoot(loader.load<Parent>())
        return loader.getController<Controller>()
    }

    private fun getRequiredResource(fxml: String): URL {
        val resourceURL = javaClass<CalendarApplication>().getResource(fxml)
        if (resourceURL == null) throw NullPointerException("No resource found for [$fxml]")
        return resourceURL
    }
}

fun main(args: Array<String>) = Application.launch(javaClass<CalendarApplication>(), * array())

