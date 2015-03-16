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

class FormApplication : Application() {
    override fun start(primaryStage: Stage) {
        primaryStage.setTitle("JavaFX Welcome")
        primaryStage.setScene(scene())
        primaryStage.show()
    }

    fun scene(): Scene {
        val scene = Scene(gridPane(), 300.0, 275.0)
        scene.getStylesheets().add(javaClass<FormApplication>().getResource("login.css").toExternalForm());
        return scene
    }

    private fun gridPane(): GridPane {
        val grid = GridPane()
        grid.setAlignment(Pos.CENTER)
        grid.setHgap(10.0)
        grid.setVgap(10.0)
        grid.setPadding(Insets(25.0))
        grid.add(text(), 0, 0, 2, 1)
        grid.add(Label("User Name:"), 0, 1)
        grid.add(TextField(), 1, 1)
        grid.add(Label("Password:"), 0, 2)
        grid.add(PasswordField(), 1, 2)
        val actionTarget = Text()
        grid.add(button(actionTarget), 1, 4)
        grid.add(actionTarget, 1, 6);
        return grid
    }

    private fun button(text: Text): HBox {
        val button = Button("Sign in")
        val buttonBox = HBox(10.0)
        button.setOnAction({(event ) ->
            text.setText("Sign in button pressed")
        });
        buttonBox.setAlignment(Pos.BOTTOM_RIGHT)
        buttonBox.getChildren().add(button)


        return buttonBox
    }

    private fun text(): Text {
        return Text("Welcome")
    }

}

fun main(args: Array<String>) = Application.launch(javaClass<FormApplication>(), * array())
