package com.iot.app.domain.repositories

import com.iot.app.domain.entities.Device
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface DeviceRepository : JpaRepository<Device, Long> {
    //TODO poprawić wszystkie zapytania, żeby było DELETED IS FALSE


    fun findDeviceByAddress(address: String) :Device?

    fun findDeviceByUuid(uuid: UUID): Device?

    fun findAllByDeletedIsFalseOrderByName(): List<Device>
}