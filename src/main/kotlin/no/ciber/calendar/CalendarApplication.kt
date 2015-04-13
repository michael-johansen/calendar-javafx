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
import java.util.Arrays
import java.util.Locale
import java.util.ResourceBundle

class CalendarApplication : Application() {
    val logger = LoggerFactory.getLogger(javaClass)
    val scene = Scene(Group(Label("loading")), 400.0, 400.0)
    var locale = Locale.ENGLISH;

    override fun start(primaryStage: Stage) {
        logger.info("Starting application")
        primaryStage.setScene(scene)
        primaryStage.addEventHandler(Event.ANY, applicationEventHandler())
        primaryStage.show()

        Platform.runLater({
            logger.info("Navigating to event list")
            primaryStage.fireEvent(NavigateToCalendarEventList())
        })
    }

    fun gotoView(fxml: String, vararg arguments:Any) {
        logger.info("Navigating to view $fxml, with arguments: ${Arrays.toString(arguments)}")
        fun defaultControllerFactory(clazz: Class<*>): Any {
            logger.info("Initializing controller $clazz with ${arguments.toList()}")
            val arrayOfClasss = arguments.map { it.javaClass }.copyToArray()
            return clazz.getConstructor(* arrayOfClasss).newInstance(* arguments)!!
        }

        val loader = FXMLLoader()
        loader.setBuilderFactory(JavaFXBuilderFactory())
        loader.setLocation(getRequiredResource(fxml))
        loader.setResources(ResourceBundle.getBundle("messages", locale))
        loader.setControllerFactory (::defaultControllerFactory)
        scene.setRoot(loader.load<Parent>())
    }

    private fun getRequiredResource(fxml: String): URL {
        return javaClass<CalendarApplication>().getResource(fxml)!!
    }

    private fun applicationEventHandler(): (Event) -> Unit {
        return { event ->
            when (event) {
                is NavigateToCalendarEventDetails -> gotoView(fxml = event.location, arguments = event.calendarEvent)
                is NavigateToCalendarEventList -> gotoView(fxml = event.location, arguments = locale)
                is ChangeLocale -> {
                    logger.info("Changing locale from $locale to ${event.locale}")
                    locale = event.locale
                }
            }
        }
    }
}

fun main(args: Array<String>) = Application.launch(javaClass<CalendarApplication>(), * array())

