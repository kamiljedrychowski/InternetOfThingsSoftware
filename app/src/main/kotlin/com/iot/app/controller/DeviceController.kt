package com.iot.app.controller

import com.iot.app.domain.dto.DeviceDto
import com.iot.app.domain.entities.Device
import com.iot.app.service.DeviceService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/device")
class DeviceController(
    private val deviceService: DeviceService
) {

    @GetMapping
    fun getAllDevices() = deviceService.getAllDevices()

    @GetMapping("/{id}")
    fun getDeviceById(@PathVariable id: Long) = deviceService.getDeviceById(id)

    @PostMapping
    fun addDevice(@RequestBody deviceDto: DeviceDto): ResponseEntity<Device> = deviceService.addDevice(deviceDto)

    @DeleteMapping("/{id}")
    fun removeDevice(@PathVariable id: Long) = deviceService.removeDevice(id)

    @PutMapping("{id}")
    fun updateDevice(@PathVariable id: Long) = deviceService.updateDevice(id)


    //TODO:
    // - kontrola urządzenia a) włączenie, b) wyłączenie, c) zmiana wysyłanych statusów, d) wysłanie innego configu
    // - przy pierwszym połączeniu pobierać uuid od urządzenia
    // - korzystanie z feign do communiactionService
    //
    //

}