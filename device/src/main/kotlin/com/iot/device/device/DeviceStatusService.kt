package com.iot.device.device

import com.iot.device.device.dto.DeviceStatus
import com.iot.device.device.enums.DeviceState
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class DeviceStatusService {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(DeviceStatusService::class.java)
    }

    private var deviceStatus: DeviceStatus? = null
    private var deviceState: DeviceState? = null
    private var fetched: Int = 0

    fun getRecentDeviceStatus(): DeviceStatus? {
        if (fetched > 25 || deviceState == null || deviceState == DeviceState.ERROR) {
            deviceState = DeviceState.ERROR
            deviceStatus.apply { deviceState = DeviceState.ERROR }// TODO ERRORA TEŻ trzeba jakoś zwracać
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
        deviceState = DeviceState.WORKING
        LOGGER.trace("Updated device status")
    }


}