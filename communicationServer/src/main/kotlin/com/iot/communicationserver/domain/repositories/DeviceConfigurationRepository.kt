package com.iot.communicationserver.domain.repositories

import com.iot.communicationserver.domain.entity.DeviceConfiguration
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface DeviceConfigurationRepository : MongoRepository<DeviceConfiguration, String> {

    fun findFirstByDeviceUuid(deviceUUID: UUID): DeviceConfiguration?

}
