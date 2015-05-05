package no.ciber.calendar.controllers

import org.slf4j.Logger

class LogBridge(val logger: Logger) {
    public fun log(message: String) {
        logger.info(message)
    }
}