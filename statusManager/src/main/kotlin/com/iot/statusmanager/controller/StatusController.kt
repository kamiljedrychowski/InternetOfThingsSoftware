package com.iot.statusmanager.controller

import com.iot.statusmanager.domain.entities.Status
import com.iot.statusmanager.domain.repositories.StatusRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/status")
class StatusController(
    private val statusRepository: StatusRepository) {

    val log: Logger = LoggerFactory.getLogger(this.javaClass)

    @PostMapping
    fun addStatus(@RequestBody status: Status): ResponseEntity<HttpStatus> {
        statusRepository.save(status)
        log.info("Saved status: $status")
        return ResponseEntity.ok().build()
    }

    @GetMapping("/test")
    fun test(): ResponseEntity<HttpStatus> {
        log.info("DZIAłAM~!!!!")
        return ResponseEntity.ok().build()
    }

    @GetMapping
    fun getAllStatuses(): ResponseEntity<List<Status>> {
        return ResponseEntity.ok(statusRepository.findAll())
    }

}