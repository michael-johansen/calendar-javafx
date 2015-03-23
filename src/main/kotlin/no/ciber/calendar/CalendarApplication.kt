package no.ciber.calendar

import javafx.application.Application
import javafx.application.Platform
import javafx.stage.Stage
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.layout.AnchorPane
import javafx.fxml.JavaFXBuilderFactory
import javafx.fxml.FXMLLoader
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.paint.Paint
import java.net.URL
import java.util.function.Consumer

class CalendarApplication : Application() {
    val scene = Scene(Group(), 400.0, 400.0)

    override fun start(primaryStage: Stage) {
        primaryStage.setScene(scene)
        gotoView<EventListController>("event-list.fxml")
        primaryStage.show()
    }


    fun <Controller : BaseController> gotoView(
            fxml: String,
            callback: (Controller) -> Unit = {it}) {
        val loader = FXMLLoader()
        loader.setControllerFactory {
            val instance = it.getConstructor(javaClass<CalendarApplication>()).newInstance(this) as Controller
            if(callback != null) callback(instance)
            instance
        }
        loader.setBuilderFactory(JavaFXBuilderFactory())
        loader.setLocation(getRequiredResource(fxml))

        scene.setRoot(loader.load<Parent>())
    }

    private fun getRequiredResource(fxml: String): URL {
        val resourceURL = javaClass<CalendarApplication>().getResource(fxml)
        if (resourceURL == null) throw NullPointerException("No resource found for [$fxml]")
        return resourceURL
    }
}

fun main(args: Array<String>) = Application.launch(javaClass<CalendarApplication>(), * array())

