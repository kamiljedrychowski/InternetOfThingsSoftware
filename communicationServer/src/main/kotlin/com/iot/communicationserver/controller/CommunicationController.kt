package com.iot.communicationserver.controller

import com.iot.communicationserver.grpc.DeviceClientManager
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/communication")
class CommunicationController(
    private val deviceClientManager: DeviceClientManager
) {
    @GetMapping("/turn/{status}/{uuid}")
    fun turnDevice(@PathVariable status: String, @PathVariable uuid: UUID): ResponseEntity<HttpStatus> {
        deviceClientManager.changeClientStatus(uuid, status)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        return ResponseEntity.ok().build()
    }

    @GetMapping("/first-connect")
    fun establishFirstConnectionToDevice(@RequestParam address: String, @RequestParam port: Int): ResponseEntity<UUID> {
        return ResponseEntity.ok(deviceClientManager.addNewClient(address, port))
    }

    //TODO: dorobić automatyczne wysyłanie i zapisywanie configu w głównym czy tutaj baza?! dodatkowa tabela?
    @PostMapping("/config/{uuid}")
    fun updateDeviceConfig(@PathVariable uuid: UUID, @RequestBody config: Any): ResponseEntity<HttpStatus> {
        return deviceClientManager.setConfig(uuid, config)
    }

}