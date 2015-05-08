package no.ciber.calendar.controllers

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import no.ciber.calendar.UserService
import no.ciber.calendar.model.AuthenticatedData
import org.slf4j.LoggerFactory
import org.w3c.dom.Document

class LoginChangeListener : ChangeListener<Document> {
    private val logger = LoggerFactory.getLogger(javaClass)
    val credentialsProperty: SimpleObjectProperty<AuthenticatedData?> = SimpleObjectProperty(null)

    override fun changed(observable: ObservableValue<out Document>?, oldValue: Document?, newValue: Document?) {
        val parameters = getParameters(newValue)
        if (parameters.containsKey("state") && parameters.containsKey("code")) {
            val data = UserService.getAuthorizationData(parameters.get("state"), parameters.get("code"))
            credentialsProperty.set(data)
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
}