package no.ciber.examples.login

import javafx.application.Application
import javafx.stage.Stage
import javafx.scene.layout.GridPane
import javafx.scene.Scene
import javafx.geometry.Pos
import javafx.geometry.Insets
import javafx.scene.text.Text
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.control.PasswordField
import javafx.scene.control.Button
import javafx.scene.layout.HBox
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.paint.Color
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import java.net.URL

class FormApplication : Application() {
    override fun start(stage: Stage) {
        val root = FXMLLoader.load<Parent>(getResource("login.fxml"))
        val scene = Scene(root, 300.0, 275.0)
        stage.setTitle("Welcome")
        stage.setScene(scene)
        stage.show()
    }

    fun getResource(path: String): URL {
        return javaClass<FormApplication>().getResource(path)
    }
}

fun main(args: Array<String>) = Application.launch(javaClass<FormApplication>(), * arrayOf())
