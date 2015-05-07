package no.ciber.calendar

import javafx.application.Application
import javafx.application.Platform
import javafx.beans.property.SimpleObjectProperty
import javafx.event.Event
import javafx.fxml.FXMLLoader
import javafx.fxml.JavaFXBuilderFactory
import javafx.scene.Group
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.layout.BorderPane
import javafx.stage.Stage
import no.ciber.calendar.model.AuthenticatedData
import no.ciber.calendar.model.SearchMode
import org.slf4j.LoggerFactory
import java.net.URL
import java.util.Locale
import java.util.ResourceBundle

class CalendarApplication : Application() {
    val logger = LoggerFactory.getLogger(javaClass)
    val scene = Scene(Group(Label("loading")), 600.0, 800.0)
    var frame: BorderPane = BorderPane()
    var locale = Locale.ENGLISH;
    var credentialsProperty = SimpleObjectProperty<AuthenticatedData>(null)

    override fun start(primaryStage: Stage) {
        logger.info("Starting application")
        primaryStage.setScene(scene)
        primaryStage.addEventHandler(Event.ANY, applicationEventHandler())
        primaryStage.show()

        Platform.runLater({
            val frame = loadParent("layouts/frame.fxml", credentialsProperty) as BorderPane
            this.frame = frame
            scene.setRoot(frame)

            logger.info("Navigating to event list")
            primaryStage.fireEvent(NavigateToLogin())
        })
    }

    fun gotoView(fxml: String, vararg arguments: Any) {
        Platform.runLater({
            frame.setCenter(loadParent(fxml, *arguments))
        })
    }

    private fun loadParent(fxml: String, vararg arguments: Any): Parent? {
        logger.info("Navigating to view $fxml, with arguments: ${arguments.toList()}")
        fun defaultControllerFactory(clazz: Class<*>): Any {
            logger.info("Initializing controller $clazz with ${arguments.toList()}")
            val argumentTypes = arguments.map { it.javaClass }.copyToArray()
            return clazz.getConstructor(* argumentTypes).newInstance(* arguments)!!
        }

        val loader = FXMLLoader()
        loader.setBuilderFactory(JavaFXBuilderFactory())
        loader.setLocation(getRequiredResource(fxml))
        loader.setResources(ResourceBundle.getBundle("messages", locale))
        loader.setControllerFactory (::defaultControllerFactory)
        val parent = loader.load<Parent>()
        return parent
    }


    private fun getRequiredResource(fxml: String): URL {
        return javaClass<CalendarApplication>().getResource(fxml)!!
    }

    private fun applicationEventHandler(): (Event) -> Unit {
        return { event ->
            when (event) {
                is NavigateToLogin -> gotoView(fxml = event.layoutLocation)
                is UserAuthenticated ->{
                    credentialsProperty.set(event.authentication)
                    Event.fireEvent(scene, NavigateToCalendarEventList(SearchMode.All))
                }
                is NavigateToCalendarEventDetails -> gotoView(fxml = event.layoutLocation, arguments = event.calendarEvent)
                is NavigateToCalendarEventList -> gotoView(fxml = event.layoutLocation, arguments = * array(locale, event.searchMode))
                is NavigateToAddUsersToEvent -> gotoView(fxml = event.layoutLocation, arguments = event.calendarEvent)
                is NavigateToCreateUser -> gotoView(fxml = event.layoutLocation, arguments = * array(event.calendarEvent, event.user))
                is NavigateToCreateLocation -> gotoView(fxml = event.layoutLocation, arguments = * array(event.calendarEvent, event.location))
                is ChangeLocale -> {
                    logger.info("Changing locale from $locale to ${event.locale}")
                    locale = event.locale
                }

            }
        }
    }
}

fun main(args: Array<String>) = Application.launch(javaClass<CalendarApplication>(), * array())

