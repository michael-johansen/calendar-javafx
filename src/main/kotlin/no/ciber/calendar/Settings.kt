package no.ciber.calendar

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.URL
import java.util.Properties


object Settings {
    private val properties: Properties = Properties();
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    init {
        fun getURL(url: String?) = if (url == null) null else URL(url)
        fun loadIfUrl(url: URL?): Unit = if (url != null) properties.load(url.openStream())
        loadIfUrl(classLoader().getResource("application.properties"))
        loadIfUrl(classLoader().getResource("calendar-external.properties"))
        loadIfUrl(getURL(System.getProperty("external.properties")))
        loadIfUrl(getURL(System.getenv("external.properties")))
    }


    public val eventServiceUrl: String = "http://ciber-event-aggregator.herokuapp.com/"

    fun oauthLoginUrl(): String = lookupProperty("oauth.login.url")!!

    fun oauthVerifyLoginUrl(): String = lookupProperty("oauth.verify.login.url")!!

    fun oauthClientSecret(): String = lookupProperty("oauth.client.secret")!!

    fun oauthClientId(): String = lookupProperty("oauth.client.id")!!

    fun oauthRedirectUri(): String = lookupProperty("oauth.redirect.url")!!

    fun oauthScope(): String = lookupProperty("oauth.scope")!!


    private fun lookupProperty(key: String): String? {
        val value = System.getenv().get(key) ?:
                System.getProperty(key) ?:
                properties.getProperty(key)
        return if (value == null) {
            logger.warn("No value found for key $key in any property sources")
            null
        } else value
    }

    private fun classLoader() = javaClass.getClassLoader()

    fun useMockApi(): Boolean {
        return true
    }
}