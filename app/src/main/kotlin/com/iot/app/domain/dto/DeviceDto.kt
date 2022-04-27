package com.iot.app.domain.dto

import com.iot.app.domain.enums.DeviceType

data class DeviceDto (
    var type: DeviceType? = null,
    var name: String? = null,
    var description: String? = null,
    var address: String? = null,
    var port: Int? = null
)
