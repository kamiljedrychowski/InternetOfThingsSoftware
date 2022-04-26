package com.iot.device.device.dto

import com.iot.device.device.enums.DeviceState

data class DeviceStatus(
    private val data: String
) {
    var temperature: Double? = null
    var humidity: Double? = null
    var timestamp: Long? = null
    var state: DeviceState? = null

    fun translateData(): DeviceStatus {
        val split = this.data.split(";")
        this.temperature = split[0].toDouble()
        this.humidity = split[1].toDouble()
        this.timestamp = System.currentTimeMillis()
        return this
    }

}
