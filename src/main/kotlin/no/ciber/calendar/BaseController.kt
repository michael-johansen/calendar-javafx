package no.ciber.calendar

import javafx.fxml.FXMLLoader
import javafx.fxml.JavaFXBuilderFactory
import javafx.scene.Group
import javafx.scene.Node
import javafx.util.Callback
import java.util.function.Consumer

open class BaseController(val application: CalendarApplication) {
    fun <Controller : BaseController> gotoScene(
            fxml: String,
            callback: (Controller) -> Unit = { it }): Unit {
        application.gotoView(fxml, callback)
    }

    fun gotoScene(fxml: String): Unit {
        application.gotoView<BaseController>(fxml)
    }
}