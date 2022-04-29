package com.iot.communicationserver.controller

import com.iot.communicationserver.domain.dto.ChangeClientStatusDto
import com.iot.communicationserver.service.DeviceClientManager
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/communication")
class CommunicationController(
    private val deviceClientManager: DeviceClientManager
) {

    @PostMapping("/turn-device")
    fun turnDevice(@RequestBody changeClientStatusDto: ChangeClientStatusDto): ResponseEntity<HttpStatus> {
        return deviceClientManager.changeClientStatus(changeClientStatusDto)
    }

    @GetMapping("/first-connect")
    fun establishFirstConnectionToDevice(@RequestParam address: String, @RequestParam port: Int): ResponseEntity<UUID> {
        return ResponseEntity.ok(deviceClientManager.addNewClient(address, port))
    }

    @PostMapping("/config")
    suspend fun updateDeviceConfig(@RequestParam uuid: UUID, @RequestBody config: Any): ResponseEntity<HttpStatus> {
        return deviceClientManager.setConfig(uuid, config)
    }

}
