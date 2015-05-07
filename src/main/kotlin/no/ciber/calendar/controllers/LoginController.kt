package no.ciber.calendar.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.mashape.unirest.http.Unirest
import javafx.application.Platform
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.event.Event
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.Node
import javafx.scene.web.WebView
import no.ciber.calendar.Settings
import no.ciber.calendar.UserAuthenticated
import no.ciber.calendar.model.AuthenticatedData
import no.ciber.util.URLBuilder
import org.slf4j.LoggerFactory
import org.w3c.dom.Document
import sun.plugin.dom.exception.InvalidStateException
import java.io.File
import java.math.BigInteger
import java.net.URL
import java.security.SecureRandom
import java.util.ResourceBundle

/**
 * Created by Michael on 06.05.2015.
 */
public class LoginController : Initializable {
    private val logger = LoggerFactory.getLogger(javaClass)
    FXML var loginWebView: WebView? = null

    override fun initialize(location: URL, resources: ResourceBundle) {
        if (!logInWithRefreshToken()) {
            logInWithWebView()
        }
    }

    private fun logInWithWebView() {
        val webEngine = loginWebView!!.getEngine()
        logger.info("Creating webView to attempt authentication.")

        val csrf = createCSRFToken()
        webEngine.documentProperty().addListener(LoginChangeListener(csrf, loginWebView!!))
        webEngine.load(generateLoginUrl(csrf))
    }

    private fun logInWithRefreshToken(): Boolean {
        val refreshToken = getRefreshToken()
        if (refreshToken != null) {
            logger.info("Found refresh token ${refreshToken} attempting login.")
            val data = refreshAuthorizationData(refreshToken)
            if (data != null) {
                Platform.runLater({
                    Event.fireEvent(loginWebView, UserAuthenticated(data))
                })
                return true
            }
        }
        return false
    }

    private fun getRefreshToken(): String? {
        val path = System.getProperty("java.io.tmpdir");
        val list = File(path)
                .listFiles({ file, name -> name.matches("""^calendar-javafx-refresh-\w+\.json$""") })
                .sortBy(comparator { left, right -> (left.lastModified() - right.lastModified()).toInt() })
        list.drop(1).forEach { it.delete() }
        return list.firstOrNull()?.readText("UTF-8")
    }

    private fun refreshAuthorizationData(refreshToken: String?): AuthenticatedData? {
        val response = Unirest.post(Settings.oauthVerifyLoginUrl())
                .field("client_id", Settings.oauthClientId())
                .field("client_secret", Settings.oauthClientSecret())
                .field("refresh_token", refreshToken)
                .field("grant_type", "refresh_token")
                .asString()
        if (!response.getStatus().equals(200)) return null;
        val json = response
                .getBody()
        return ObjectMapper().readValue(json, javaClass<AuthenticatedData>())
    }


    private fun generateLoginUrl(csrf: String): String {
        val url = URLBuilder(Settings.oauthLoginUrl())
                .with("response_type", "code")
                .with("client_id", Settings.oauthClientId())
                .with("redirect_uri", Settings.oauthRedirectUri())
                .with("scope", Settings.oauthScope())
                .with("state", csrf)
                .with("login_hint", hintEmail())
                .with("include_granted_scopes", true).toString()

        logger.info("Generated login url: $url")
        return url
    }

    private fun hintEmail(): String {
        // TODO: check local storage?
        return ""
    }

    private fun createCSRFToken(): String {
        return "calendar-javafx-${BigInteger(512, SecureRandom()).toString(36)}"
    }


}

