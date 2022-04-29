package com.iot.app.service

import com.iot.app.domain.enums.DeviceStatus
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.util.CollectionUtils

@Service
class DeviceStatusScheduler(private val deviceService: DeviceService) {

    //TODO scheduler:
    // - running every 5 min
    // - fetching connected devices
    // - fetching most recent status - if needed setting ERROR state
    // - to change error we can disconnect or restart device -> then correct status

    @Scheduled(fixedDelayString  = "\${deviceStatusSchedulerMilliseconds}")
    fun deviceStatusScheduler() {
        val devices = deviceService.getDeviceByStatusIn(DeviceStatus.connectedStatuses.toList())
        if(!CollectionUtils.isEmpty(devices)) {
            devices.forEach {
            }
        }
    }

}
