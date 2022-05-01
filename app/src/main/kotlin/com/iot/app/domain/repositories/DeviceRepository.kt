package com.iot.app.domain.repositories

import com.iot.app.domain.entities.Device
import com.iot.app.domain.enums.DeviceStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface DeviceRepository : JpaRepository<Device, Long> {
    fun findDeviceByIdAndDeletedIsFalse(id: Long): Optional<Device>

    fun findDeviceByAddressAndPortAndDeletedIsFalse(address: String, port: Int) :Device?

    fun findAllByDeletedIsFalseOrderByName(): List<Device>

    fun findAllByStatusInAndDeletedIsFalse(deviceStatuses: List<DeviceStatus>): List<Device>
}
