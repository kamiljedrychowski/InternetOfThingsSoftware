package com.iot.statusmanager

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class StatusManagerApplication

fun main(args: Array<String>) {
    runApplication<StatusManagerApplication>(*args)
}
