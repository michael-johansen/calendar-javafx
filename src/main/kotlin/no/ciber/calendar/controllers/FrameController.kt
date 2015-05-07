package no.ciber.calendar.controllers

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ChangeListener
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import no.ciber.calendar.model.AuthenticatedData

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
        credentials.addListener { observableValue, v1, v2 ->
            if (v2 != null) {
                userImageView!!.setImage(Image("https://www.googleapis.com/plus/v1/people/me?access_token=${v2.accessToken}"))
            }
        }
    }
}
