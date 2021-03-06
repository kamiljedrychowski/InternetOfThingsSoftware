package com.iot.app.feign

import com.iot.app.feign.dto.StatusDto
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import java.util.*

@FeignClient("statusManager")
interface StatusManagerFeignClient {

    @GetMapping("/status")
    fun getAllStatuses(): ResponseEntity<List<StatusDto>>

    @GetMapping("/status/{deviceUuid}")
    fun getAllStatusesByDevice(@PathVariable deviceUuid: UUID): ResponseEntity<List<StatusDto>>

    @GetMapping("/status/last/{deviceUuid}")
    fun getLastStatusByDevice(@PathVariable deviceUuid: UUID): ResponseEntity<StatusDto>

}
