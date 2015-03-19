package no.ciber.calendar

import javafx.application.Application
import javafx.stage.Stage
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.layout.AnchorPane
import javafx.fxml.JavaFXBuilderFactory
import javafx.fxml.FXMLLoader

class CalendarApplication : Application() {
    override fun start(primaryStage: Stage) {
        val scene = Scene(Label("Loading"))
        primaryStage.setScene(scene)
        replaceSceneContent("my-events.fxml", scene)
        primaryStage.show()
    }

    private fun replaceSceneContent(fxml: String, scene : Scene) {
        val loader = FXMLLoader()
        val inputStream = javaClass<CalendarApplication>().getResourceAsStream(fxml)
        loader.setBuilderFactory(JavaFXBuilderFactory())
        loader.setLocation(javaClass<CalendarApplication>().getResource(fxml))
        try {
            val page = loader.load<AnchorPane>(inputStream)
            scene.setRoot(page)
        } finally {
            inputStream.close()
        }
    }
}

fun main(args: Array<String>) = Application.launch(javaClass<CalendarApplication>(), * array())

