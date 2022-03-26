package com.iot.app.controller

import com.iot.app.domain.dto.DeviceDto
import com.iot.app.service.DeviceService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/device")
class DeviceController(
    private val deviceService: DeviceService
) {

    @PostMapping
    fun addDevice(@RequestBody deviceDto: DeviceDto): ResponseEntity<HttpStatus> = deviceService.addDevice(deviceDto)

    @DeleteMapping("/{id}")
    fun removeDevice(@PathVariable id: Long) = deviceService.removeDevice(id)

    @GetMapping
    fun getAllDevices() = deviceService.getAllDevices()

}