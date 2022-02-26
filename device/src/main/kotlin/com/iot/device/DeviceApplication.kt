package com.iot.device

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DeviceApplication

fun main(args: Array<String>) {
    val port = System.getenv("PORT")?.toInt() ?: 50051
    val server = DeviceGrpcServer(port)
    server.start()
    server.blockUntilShutdown()

    runApplication<DeviceApplication>(*args)
}
