package no.ciber.calendar.controllers

import com.mashape.unirest.http.Unirest
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ChangeListener
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import no.ciber.calendar.UserService
import no.ciber.calendar.model.AuthenticatedData
import no.ciber.calendar.repositories.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.net.URL
import java.util.ResourceBundle

/**
 * User: Michael Johansen
 * Date: 07.05.2015
 * Time: 15:32
 */
public class FrameController(val credentials: SimpleObjectProperty<AuthenticatedData>) : Initializable {
    FXML public var navigationTextField: TextField? = null
    FXML public var userLabel: Label? = null
    FXML public var userImageView: ImageView? = null

    override fun initialize(location: URL, resources: ResourceBundle) {
        userLabel!!.textProperty().bind(UserService.displayNameProperty)
        userImageView!!.imageProperty().bind(UserService.userImageProperty)
    }
}
