package no.ciber.util

import no.ciber.util.Parameter
import java.net.URLEncoder

class URLBuilder(val url: String, vararg val parameters: Parameter) {

    fun with(key: String, vararg values: Any): URLBuilder = URLBuilder(url, *parameters, Parameter(key, *values))

    override fun toString(): String {
        return if (parameters.isEmpty())
            "$url"
        else
            "$url?${parameters.map { parameter -> parameter.values.map { value -> "${parameter.key}=${value}" } }.flatMap { it }.joinToString("&")}"
    }
}