package no.ciber.calendar

import javafx.fxml.Initializable
import java.net.URL
import java.util.ResourceBundle
import javafx.scene.control.Label
import javafx.fxml.FXML

class EventDetailController:Initializable{
    [FXML]
    private val eventLabel  = Label();

    override fun initialize(location: URL?, resources: ResourceBundle?) {

    }

}