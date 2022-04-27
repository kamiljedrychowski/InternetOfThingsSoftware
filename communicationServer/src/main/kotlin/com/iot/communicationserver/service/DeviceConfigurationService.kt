package com.iot.communicationserver.service

import com.iot.communicationserver.domain.entity.DeviceConfiguration
import com.iot.communicationserver.domain.repositories.DeviceConfigurationRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

@Service
class DeviceConfigurationService(
    private val deviceConfigurationRepository: DeviceConfigurationRepository
) {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(DeviceConfigurationService::class.java)
    }

    fun getDeviceConfig(deviceUuid: UUID): DeviceConfiguration? {
        return deviceConfigurationRepository.findFirstByDeviceUuid(deviceUuid)
    }

    fun addOrUpdateDeviceConfig(deviceUuid: UUID, newConfig: Any): DeviceConfiguration {
        val config = deviceConfigurationRepository.findFirstByDeviceUuid(deviceUuid)
        return if (config == null) {
            createNewConfig(deviceUuid, newConfig)
        } else {
            updateConfig(config, newConfig)
        }
    }

    fun createNewConfig(deviceUuid: UUID, newConfig: Any): DeviceConfiguration {
        val config = DeviceConfiguration(
            modificationDate = LocalDateTime.now(ZoneOffset.UTC),
            deviceUuid = deviceUuid,
            details = newConfig
        )
        LOGGER.debug("Created new config for device: $config")
        return deviceConfigurationRepository.save(config)
    }

    fun updateConfig(config: DeviceConfiguration, newConfig: Any): DeviceConfiguration {
        LOGGER.debug("Updating config for device ${config.deviceUuid}: $newConfig}")
        config.details = newConfig
        return deviceConfigurationRepository.save(config)
    }

}
