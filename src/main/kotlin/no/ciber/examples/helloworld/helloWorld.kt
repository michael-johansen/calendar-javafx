package helloworld

import javafx.application.Application
import javafx.stage.Stage
import javafx.scene.layout.StackPane
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.event.EventHandler
import javafx.event.ActionEvent

class HelloWorldApplication : Application() {

    override fun start(primaryStage: Stage) {
        val btn = Button("Say 'Hello World'");
        btn.setOnAction(object : EventHandler<ActionEvent> {
            override fun handle(event: ActionEvent?) = println("Hello World!")
        });

        val root = StackPane(btn);

        val scene = Scene(root, 300.0, 250.0);
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

fun main(args: Array<String>) {
    Application.launch(javaClass<HelloWorldApplication>(), * arrayOf())
}