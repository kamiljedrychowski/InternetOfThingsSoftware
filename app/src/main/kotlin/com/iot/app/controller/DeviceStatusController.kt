package com.iot.app.controller

import com.iot.app.feign.StatusManagerFeignClient
import com.iot.app.feign.dto.StatusDto
import com.iot.app.service.DeviceService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/status")
class DeviceStatusController(
    private val statusManagerFeignClient: StatusManagerFeignClient,
    private val deviceService: DeviceService
) {

    @GetMapping
    fun getAllStatuses(): ResponseEntity<List<StatusDto>> = statusManagerFeignClient.getAllStatuses()

    @GetMapping("/{deviceId}")
    fun getAllStatusesByDevice(@PathVariable deviceId: Long): ResponseEntity<List<StatusDto>> {
        val device = deviceService.getDeviceById(deviceId)
        return if(device.statusCode.is2xxSuccessful) {
            statusManagerFeignClient.getAllStatusesByDevice(device.body!!.uuid!!)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    @GetMapping("/last/{deviceId}")
    fun getLastStatusByDevice(@PathVariable deviceId: Long): ResponseEntity<StatusDto> {
        val device = deviceService.getDeviceById(deviceId)
        return if(device.statusCode.is2xxSuccessful) {
            statusManagerFeignClient.getLastStatusByDevice(device.body!!.uuid!!)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

}
