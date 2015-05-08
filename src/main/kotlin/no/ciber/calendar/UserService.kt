package no.ciber.calendar

import com.fasterxml.jackson.databind.ObjectMapper
import com.mashape.unirest.http.JsonNode
import com.mashape.unirest.http.Unirest
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.image.Image
import no.ciber.calendar.model.AuthenticatedData
import no.ciber.util.URLBuilder
import org.slf4j.LoggerFactory
import sun.plugin.dom.exception.InvalidStateException
import java.io.File
import java.math.BigInteger
import java.security.SecureRandom
import java.time.Instant

/**
 * User: Michael Johansen
 * Date: 08.05.2015
 * Time: 12:41
 */
public object UserService {
    private val logger = LoggerFactory.getLogger(javaClass)
    val displayNameProperty: SimpleStringProperty = SimpleStringProperty("Not signed in yet")
    val userImageProperty: SimpleObjectProperty<Image> = SimpleObjectProperty<Image>(null)
    val credentialsProperty: SimpleObjectProperty<AuthenticatedData> = SimpleObjectProperty<AuthenticatedData>(null)

    init {
        credentialsProperty.addListener { observableValue, v1, v2 ->
            if (v2 != null) {
                val userData = getUserData(v2)
                displayNameProperty.set(getDisplayName(userData))
                userImageProperty.set(getImage(userData))
            }
        }
    }

    private fun getUserData(v2: AuthenticatedData): JsonNode? {
        val url = "https://www.googleapis.com/plus/v1/people/me?access_token=${v2.accessToken}"
        logger.info("Fetching user data from $url")
        return  Unirest.get(url).asJson().getBody()
    }

    private fun getDisplayName(userData: JsonNode?) = userData?.getObject()?.getString("displayName")

    private fun getImage(userData: JsonNode?): Image {
        val imageUrl = userData?.getObject()?.getJSONObject("image")?.getString("url")
        // TODO: Fallback to local generic image
        logger.info("Fetching user image from $imageUrl")
        return Image(imageUrl)
    }

    fun logInWithRefreshToken(): AuthenticatedData? {
        val refreshToken = getRefreshToken()
        return if (refreshToken != null) {
            logger.info("Found refresh token ${refreshToken} attempting login.")
            refreshAuthorizationData(refreshToken)
        } else null
    }

    private fun getRefreshToken(): String? {
        val path = System.getProperty("java.io.tmpdir");
        val list = File(path)
                .listFiles({ file, name -> name.matches("""^calendar-javafx-refresh-\w+\.json$""") })
                .sortBy { it.lastModified() }
                .reverse()
        list.forEach { logger.info("Refresh token from: ${Instant.ofEpochMilli(it.lastModified())} in file: ${it.name}") }
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
        val success = response.getStatus().equals(200)

        logger.info("Login with refresh was token successful: $success")
        if (!success) return null;
        return ObjectMapper().readValue(response.getBody(), javaClass<AuthenticatedData>())
    }

    fun generateLoginUrl(): String {
        val url = URLBuilder(Settings.oauthLoginUrl())
                .with("response_type", "code")
                .with("client_id", Settings.oauthClientId())
                .with("redirect_uri", Settings.oauthRedirectUri())
                .with("scope", Settings.oauthScope())
                .with("state", createCSRFToken())
                .with("login_hint", hintEmail())
                .with("include_granted_scopes", true).toString()

        logger.info("Generated login url: $url")
        return url
    }

    fun hintEmail(): String {
        // TODO: check local storage?
        return ""
    }

    private var csrf = ""

    fun createCSRFToken(): String {
        csrf = "calendar-javafx-${BigInteger(512, SecureRandom()).toString(36)}"
        return csrf
    }

    fun verifyCSRF(csrf: String?) {
        logger.info("Checking csrf")
        if (!this.csrf.equals(csrf)) throw InvalidStateException("CSRF mismatch")
    }

    fun getAuthorizationData(csrf: String?, token: String?):AuthenticatedData {
        verifyCSRF(csrf)

        val authenticatedData = getAuthorizationData(token)
        storeRefreshToken(authenticatedData.refreshToken!!);
        return authenticatedData
    }

    private fun storeRefreshToken(refreshToken: String) {
        File.createTempFile("calendar-javafx-refresh-", ".json").writeText(refreshToken, "UTF-8")
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