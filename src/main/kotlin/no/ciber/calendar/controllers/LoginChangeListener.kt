package no.ciber.calendar.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.mashape.unirest.http.Unirest
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.event.Event
import javafx.scene.Node
import no.ciber.calendar.Settings
import no.ciber.calendar.UserAuthenticated
import no.ciber.calendar.model.AuthenticatedData
import org.slf4j.LoggerFactory
import org.w3c.dom.Document
import sun.plugin.dom.exception.InvalidStateException
import java.io.File

class LoginChangeListener(val csrf: String, val node: Node) : ChangeListener<Document> {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun changed(observable: ObservableValue<out Document>?, oldValue: Document?, newValue: Document?) {
        val parameters = getParameters(newValue)
        if (parameters.containsKey("state") && parameters.containsKey("code")) {
            verifyCSRF(csrf, parameters.get("state"))

            val data = getAuthorizationData(parameters.get("code"))
            storeRefreshToken(data.accessToken!!);
            Event.fireEvent(node, UserAuthenticated(data))
        } else {
            logger.info("User is not yet logged in. Waiting.")
        }
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

    private fun storeRefreshToken(refreshToken: String) {
        File.createTempFile("calendar-javafx-refresh-", ".json").writeText(refreshToken, "UTF-8")
    }

    private fun verifyCSRF(csrf: String, state: String?) {
        logger.info("Checking csrf")
        if (!csrf.equals(state)) throw InvalidStateException("CSRF mismatch")
    }

    private fun getAuthorizationData(code: String?): AuthenticatedData {
        val json = Unirest.post(Settings.oauthVerifyLoginUrl())
                .field("code", code!!)
                .field("client_id", Settings.oauthClientId())
                .field("client_secret", Settings.oauthClientSecret())
                .field("redirect_uri", Settings.oauthRedirectUri())
                .field("grant_type", "authorization_code")
                .asString()
                .getBody()
        return ObjectMapper().readValue(json, javaClass<AuthenticatedData>())
    }
}