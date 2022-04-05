package com.iot.statusmanager.domain.dto

import com.iot.statusmanager.domain.enums.StatusType
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

data class StatusDto(
    val createdDate: LocalDateTime = LocalDateTime.now(ZoneOffset.UTC),
    val statusDate: LocalDateTime?,
    val statusType: StatusType,
    val deviceUuid: UUID,
    val details: Any
)
