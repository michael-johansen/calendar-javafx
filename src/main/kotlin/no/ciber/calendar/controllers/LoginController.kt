package no.ciber.calendar.controllers

import com.mashape.unirest.http.Unirest
import javafx.event.Event
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.web.WebView
import no.ciber.calendar.Settings
import no.ciber.calendar.UserAuthenticated
import no.ciber.util.URLBuilder
import org.slf4j.LoggerFactory
import org.w3c.dom.Document
import sun.plugin.dom.exception.InvalidStateException
import java.math.BigInteger

import java.net.URL
import java.net.URLEncoder
import java.security.SecureRandom
import java.util.ResourceBundle
import java.util.StringTokenizer
import java.util.UUID
import java.util.regex.Pattern
import kotlin.browser.document

/**
 * Created by Michael on 06.05.2015.
 */
public class LoginController : Initializable {
    private val logger = LoggerFactory.getLogger(javaClass)
    FXML var loginWebView: WebView? = null

    override fun initialize(location: URL, resources: ResourceBundle) {
        val webEngine = loginWebView!!.getEngine()
        val csrf = createCSRFToken()

        webEngine.documentProperty().addListener({ obs, oldValue, newValue ->
            val parameters = getParameters(newValue)
            if (parameters.containsKey("state") && parameters.containsKey("code")) {
                verifyCSRF(csrf, parameters.get("state"))
                verifyCode(parameters.get("code"))
            } else {
                logger.info("User is not yet logged in. Waiting.")
            }
        })


        webEngine.load(generateLoginUrl(csrf))
    }

    private fun verifyCSRF(csrf: String, state: String?) {
        logger.info("Checking csrf")
        if (!csrf.equals(state)) throw InvalidStateException("CSRF mismatch")
    }

    private fun verifyCode(code: String?) {
        val json = Unirest.post(Settings.oauthVerifyLoginUrl())
                .field("code", code!!)
                .field("client_id", Settings.oauthClientId())
                .field("client_secret", Settings.oauthClientSecret())
                .field("redirect_uri", Settings.oauthRedirectUri())
                .field("grant_type", "authorization_code")
                .asJson()
        logger.info("Response: $json")

        val accessToken = json
                .getBody()
                .getObject()
                .get("access_token")
                .toString()

        Event.fireEvent(loginWebView, UserAuthenticated(accessToken))
    }

    private fun getParameters(document: Document?): Map<String, String> {
        val content = document?.getElementsByTagName("title")?.item(0)?.getTextContent() ?: ""
        if (content.startsWith("Success ")) {
            return content.replaceFirst("^Success ", "")
                    .split("&")
                    .toMap { it.replaceFirst("""=.*$""", "") }
                    .mapValues { it.getValue().replaceFirst("""^\w+=""", "") }
        }

        return emptyMap()
    }

    private fun generateLoginUrl(csrf: String): String {
        val url = URLBuilder(Settings.oauthLoginUrl())
                .with("response_type", "code")
                .with("client_id", Settings.oauthClientId())
                .with("redirect_uri", Settings.oauthRedirectUri())
                .with("scope", Settings.oauthScope())
                .with("state", csrf)
                .with("login_hint", hintEmail())
                .with("include_granted_scopes", false).toString()

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


