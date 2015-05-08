package no.ciber.calendar.controllers

import javafx.application.Platform
import javafx.beans.property.SimpleObjectProperty
import javafx.event.Event
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.web.WebView
import no.ciber.calendar.UserAuthenticated
import no.ciber.calendar.UserService
import no.ciber.calendar.model.AuthenticatedData
import org.slf4j.LoggerFactory
import java.net.URL
import java.util.ResourceBundle

/**
 * Created by Michael on 06.05.2015.
 */
public class LoginController : Initializable {
    private val logger = LoggerFactory.getLogger(javaClass)
    FXML var loginWebView: WebView? = null
    private val loginChangeListener = LoginChangeListener()
    private val credentialsProperty: SimpleObjectProperty<AuthenticatedData?> = SimpleObjectProperty(null)


    override fun initialize(location: URL, resources: ResourceBundle) {
        credentialsProperty.bindBidirectional(loginChangeListener.credentialsProperty)
        credentialsProperty.addListener { observableValue, v1, v2 ->
            if (v2 != null) Platform.runLater({
                Event.fireEvent(loginWebView, UserAuthenticated(v2))
            })
        }
        credentialsProperty.set(UserService.logInWithRefreshToken())
        if (credentialsProperty.get() == null) logInWithWebView()
    }


    private fun logInWithWebView() {
        val webEngine = loginWebView!!.getEngine()
        logger.info("Creating webView to attempt authentication.")

        webEngine.documentProperty().addListener(loginChangeListener)
        webEngine.load(UserService.generateLoginUrl())
    }


}

