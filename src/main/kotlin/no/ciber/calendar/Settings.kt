package no.ciber.calendar

import javafx.beans.property.Property
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.InputStreamReader
import java.net.URL
import java.time.format.DateTimeFormatter
import java.util.Properties
import java.util.PropertyResourceBundle


object Settings {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    public val eventServiceUrl: String = "http://ciber-event-aggregator.herokuapp.com/"
    /**
     * I would like to use DateTimeFormatter.ISO_DATE_TIME, but the API doesn't recognize the fractional seconds as
     * a valid date, even if it is a part of the ISO-8601 Standard.
     */
    public fun eventDateFormat(): DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX")

    fun oauthLoginUrl(): String = lookupProperty("oauth.login.url")!!

    fun oauthVerifyLoginUrl(): String = lookupProperty("oauth.verify.login.url")!!

    fun oauthClientSecret(): String = lookupProperty("oauth.client.secret")!!

    fun oauthClientId(): String = lookupProperty("oauth.client.id")!!

    fun oauthRedirectUri(): String = lookupProperty("oauth.redirect.url")!!

    fun oauthScope(): String = lookupProperty("oauth.scope")!!

    private fun lookupProperty(key: String): String? {
        val value = System.getenv().get(key) ?:
                System.getProperty(key) ?:
                getKeyFromProperty(key, getURL(System.getenv("external.properties"))) ?:
                getKeyFromProperty(key, getURL(System.getProperty("external.properties"))) ?:
                getKeyFromProperty(key, classLoader().getResource("calendar-external.properties")) ?:
                getKeyFromProperty(key, classLoader().getResource("application.properties"))

        return if (value == null) {
            logger.warn("No value found for key $key in any property sources")
            null
        } else value
    }

    private fun classLoader() = javaClass.getClassLoader()

    private fun getURL(url:String?) = if(url == null) null else URL(url)

    private fun getKeyFromProperty(key: String, url: URL?): String? {
        return when {
            url != null -> {
                logger.debug("Looking for $key in $url")
                val properties = Properties()
                properties.load(url.openStream())
                val value = properties.getProperty(key)
                if (value == null || value.isEmpty()) {
                    null
                } else {
                    logger.debug("Found value $value for key $key in $url")
                    value
                }
            }
            else -> null
        }
    }
}