package com.iot.communicationserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CommunicationServerApplication

fun main(args: Array<String>) {
    runApplication<CommunicationServerApplication>(*args)
}

