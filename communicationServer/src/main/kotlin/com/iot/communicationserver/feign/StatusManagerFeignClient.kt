package com.iot.communicationserver.feign

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping

@FeignClient("statusManager")
interface StatusManagerFeignClient {

    @GetMapping("/status/test")
    fun test(): ResponseEntity<HttpStatus>
}