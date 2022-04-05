package com.iot.statusmanager.controller

import com.iot.statusmanager.domain.dto.StatusDto
import com.iot.statusmanager.domain.entities.Status
import com.iot.statusmanager.service.StatusService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/status")
class StatusController(
    private val statusService: StatusService
) {

    @PostMapping
    fun addStatus(@RequestBody status: StatusDto): ResponseEntity<Status> = ResponseEntity.ok(statusService.saveStatus(status))

    @GetMapping
    fun getAllStatuses(): ResponseEntity<List<Status>> = ResponseEntity.ok(statusService.getAllStatuses())

    @GetMapping("/{deviceUuid}")
    fun getAllStatusesByDevice(@PathVariable deviceUuid: UUID): ResponseEntity<List<Status>> =
        ResponseEntity.ok(statusService.getAllStatusesByDeviceUuid(deviceUuid))

    @GetMapping("/last/{deviceUuid}")
    fun getLastStatusByDevice(@PathVariable deviceUuid: UUID): ResponseEntity<Status> =
        ResponseEntity.ok(statusService.getLastStatusByDevice(deviceUuid))

}