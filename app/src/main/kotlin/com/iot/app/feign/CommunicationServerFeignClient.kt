package com.iot.app.feign

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@FeignClient("communicationServer")
interface CommunicationServerFeignClient {

    @GetMapping("/communication/connect/{uuid}")
    fun connectDevice(@PathVariable uuid: UUID): ResponseEntity<HttpStatus>

    @GetMapping("/communication/disconnect/{uuid}")
    fun disconnectDevice(@PathVariable uuid: UUID): ResponseEntity<HttpStatus>

    @GetMapping("/communication/first-connect")
    fun establishFirstConnectionToDevice(@RequestParam address: String, @RequestParam port: Int): ResponseEntity<UUID>

    @PostMapping("/communication/config")
    fun updateDeviceConfig(@RequestParam uuid: UUID, @RequestBody config: Any): ResponseEntity<HttpStatus>

}
