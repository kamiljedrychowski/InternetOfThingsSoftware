package com.iot.communicationserver.feign.dto

import com.iot.communicationserver.feign.enums.StatusType
import java.time.LocalDateTime
import java.util.*

data class StatusDto(
    val statusDate: LocalDateTime,
    val statusType: StatusType,
    val deviceUuid: UUID,
    val details: Any
)
