package com.iot.communicationserver.feign.dto

import java.time.LocalDateTime
import java.util.*

data class StatusDto(
    val statusDate: LocalDateTime,
    val statusType: String,
    val deviceUuid: UUID,
    val details: Any
)
