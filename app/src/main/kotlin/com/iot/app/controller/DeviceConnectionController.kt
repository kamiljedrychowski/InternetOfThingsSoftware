package com.iot.app.controller

import com.iot.app.domain.enums.TurnStatus
import com.iot.app.service.DeviceConnectionService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/device-connection")
class DeviceConnectionController(
    private val deviceConnectionService: DeviceConnectionService
) {

    @GetMapping("/turn-device/{id}/{turnStatus}")
    fun turnDevice(@PathVariable id: Long, @PathVariable turnStatus: TurnStatus): ResponseEntity<HttpStatus> =
        deviceConnectionService.turnDevice(id, turnStatus)

    @PostMapping("/config/{id}")
    fun updateDeviceConfig(@PathVariable id: Long, @RequestBody config: Any): ResponseEntity<HttpStatus> =
        deviceConnectionService.updateConfig(id, config)

}
