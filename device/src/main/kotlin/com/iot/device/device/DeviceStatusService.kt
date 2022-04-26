package com.iot.device.device

import com.iot.device.device.dto.DeviceStatus
import com.iot.device.device.enums.DeviceState
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service


@Service
class DeviceStatusService {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(DeviceStatusService::class.java)
        private const val ERROR_STATE_THRESHOLD = 50
    }

    private var deviceStatus: DeviceStatus? = null
    private var fetched: Int = 0

    fun getRecentDeviceStatus(): DeviceStatus? {
        if (deviceStatus == null || fetched > ERROR_STATE_THRESHOLD || deviceStatus!!.state == null || deviceStatus!!.state == DeviceState.ERROR) {
            deviceStatus!!.apply { state = DeviceState.ERROR }
            LOGGER.error("Device in error State")
            return null
        }
        fetched++
        deviceStatus!!.apply { state = DeviceState.WORKING }
        return deviceStatus
    }

    fun updateDeviceStatus(updatedDeviceStatus: DeviceStatus) {
        fetched = 0
        deviceStatus = updatedDeviceStatus
        deviceStatus!!.apply { state = DeviceState.WORKING }
        LOGGER.trace("Updated device status")
    }

}
