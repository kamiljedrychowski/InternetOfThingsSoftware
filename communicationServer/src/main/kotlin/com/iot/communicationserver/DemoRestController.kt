package com.iot.communicationserver

import kotlinx.coroutines.runBlocking
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class DemoRestController(private val deviceClient: DeviceClient) {


    @GetMapping("/test/{user}")
    fun testRest(@PathVariable user: String): String {
        greet(user)
        return user
    }

    @GetMapping("/status/{text}/{timeInterval}")
    fun testStatus(@PathVariable text: String, @PathVariable timeInterval: Long) = runBlocking{
        status(text, timeInterval)
    }

    @GetMapping("/status/stop/{reason}")
    fun stopStatus(@PathVariable reason: String) {
        stop(reason)
    }

    fun greet(user: String) = runBlocking {
        deviceClient.greet(user)
    }

    fun stop(reason: String) = runBlocking {
        deviceClient.stop(reason)
    }

    suspend fun status(text: String, timeInterval: Long) {
        deviceClient.getStatus(text, timeInterval)
    }
}