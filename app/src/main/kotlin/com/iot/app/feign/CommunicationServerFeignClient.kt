package com.iot.app.feign

import com.iot.app.domain.dto.ChangeClientStatusDto
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@FeignClient("communicationServer")
interface CommunicationServerFeignClient {

    @PostMapping("/communication/turn-device")
    fun turnDevice(@RequestBody changeClientStatusDto: ChangeClientStatusDto): ResponseEntity<HttpStatus>

    @GetMapping("/communication/first-connect")
    fun establishFirstConnectionToDevice(@RequestParam address: String, @RequestParam port: Int): ResponseEntity<UUID>

    @PostMapping("/communication/config")
    fun updateDeviceConfig(@RequestParam uuid: UUID, @RequestBody config: Any): ResponseEntity<HttpStatus>

}
