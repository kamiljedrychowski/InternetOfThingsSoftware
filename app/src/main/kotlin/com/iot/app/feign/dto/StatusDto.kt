package com.iot.app.feign.dto

import com.iot.app.feign.enums.StatusType
import java.time.LocalDateTime
import java.util.*

data class StatusDto(
    val statusDate: LocalDateTime?,
    val statusType: StatusType,
    val deviceUuid: UUID,
    val details: Any
)
