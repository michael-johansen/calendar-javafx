package no.ciber.calendar.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

JsonIgnoreProperties(ignoreUnknown = true)
class AuthenticatedData() {
    JsonProperty("access_token") var accessToken: String? = null;
    JsonProperty("refresh_token") var refreshToken: String? = null;
    JsonProperty("expires_in") var expiresIn: Long? = null;
    JsonProperty("token_type") var tokenType: String? = null;
}