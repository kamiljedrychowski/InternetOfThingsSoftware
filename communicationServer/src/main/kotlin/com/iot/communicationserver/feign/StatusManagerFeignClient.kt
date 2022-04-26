package com.iot.communicationserver.feign

import com.iot.communicationserver.feign.dto.StatusDto
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient("statusManager")
interface StatusManagerFeignClient {

    @PostMapping("/status")
    fun addStatus(@RequestBody status: StatusDto): ResponseEntity<StatusDto>

}