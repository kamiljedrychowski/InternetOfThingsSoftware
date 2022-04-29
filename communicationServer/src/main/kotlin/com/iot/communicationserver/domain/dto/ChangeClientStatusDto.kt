package com.iot.communicationserver.domain.dto

import java.util.*

data class ChangeClientStatusDto(
    val uuid: UUID,
    val address: String?,
    val port: Int?,
    val newStatus: String
)
