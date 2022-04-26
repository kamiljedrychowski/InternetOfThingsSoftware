package com.iot.app.controller

import com.iot.app.service.DeviceConnectionService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/device-connection")
class DeviceConnectionController(
    private val deviceConnectionService: DeviceConnectionService
) {

    @GetMapping("/connect/{id}")
    fun connectDevice(@PathVariable id: Long) = deviceConnectionService.connectDevice(id)


    @PostMapping("/config/{id}")
    fun updateDeviceConfig(@PathVariable id: Long, @RequestBody config: Any): ResponseEntity<HttpStatus> =
        deviceConnectionService.updateConfig(id, config)

    //TODO:
    // - kontrola urządzenia a) połączenie, b) rozłączenie, c) zmiana wysyłanych statusów, d) wysłanie innego configu
    // - przy pierwszym połączeniu pobierać uuid od urządzenia
    // - korzystanie z feign do communiactionService
    //
    //

}