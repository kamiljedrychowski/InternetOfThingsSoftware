package com.iot.app.service

import com.iot.app.domain.dto.DeviceDto
import com.iot.app.domain.entities.Device
import com.iot.app.domain.enums.DeviceStatus
import com.iot.app.domain.repositories.DeviceRepository
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneOffset

@Service
class DeviceService(
    private val deviceRepository: DeviceRepository
) {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(DeviceService::class.java)
    }

    fun addDevice(deviceDto: DeviceDto): ResponseEntity<HttpStatus> {
        if (deviceDto.address.isNullOrBlank() || deviceDto.name.isNullOrBlank() || deviceDto.type == null) {
            LOGGER.error("DeviceDto is invalid $deviceDto")
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build()
        }
        val deviceByAddress = deviceRepository.findDeviceByAddress(deviceDto.address!!)
        if (deviceByAddress != null) {
            LOGGER.error("Device with this address already exists: $deviceDto")
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).build()
        }

        val device = Device(createdDate = LocalDateTime.now(ZoneOffset.UTC)).apply {
            modifiedDate = LocalDateTime.now(ZoneOffset.UTC)
            type = deviceDto.type
            name = deviceDto.name
            status = DeviceStatus.DISCONNECTED
            description = deviceDto.description
            address = deviceDto.address
        }

        deviceRepository.save(device)
        LOGGER.debug("Created and saved device: $device")

        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    fun removeDevice(id: Long): ResponseEntity<HttpStatus> {
        val device = deviceRepository.findById(id)
        if(device.isEmpty) {
            LOGGER.error("Device with id: $id does not exist")
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
        if(device.get().status?.equals(DeviceStatus.CONNECTED) == true) {
            //TODO disconnect device
            LOGGER.debug("Device disconnected")
        }

        device.get().modifiedDate = LocalDateTime.now(ZoneOffset.UTC)
        device.get().apply {
            modifiedDate = LocalDateTime.now(ZoneOffset.UTC)
            deleted = true
        }

        deviceRepository.save(device.get())
        LOGGER.debug("Device with id: $id deleted")
        return ResponseEntity.ok().build()
    }

    fun getAllDevices(): ResponseEntity<List<Device>> {
        return ResponseEntity.ok(deviceRepository.findAllByDeletedIsFalseOrderByName())
    }







}