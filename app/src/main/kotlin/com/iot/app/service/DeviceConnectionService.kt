package com.iot.app.service

import com.iot.app.domain.entities.Device
import com.iot.app.domain.enums.DeviceStatus
import com.iot.app.domain.enums.TurnStatus
import com.iot.app.domain.repositories.DeviceRepository
import com.iot.app.feign.CommunicationServerFeignClient
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class DeviceConnectionService(
    private val deviceRepository: DeviceRepository,
    private val communicationServerFeignClient: CommunicationServerFeignClient
) {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(DeviceConnectionService::class.java)
    }

    fun turnDevice(id: Long, turnStatus: TurnStatus): ResponseEntity<HttpStatus> {
        return when(turnStatus) {
            TurnStatus.ON -> turnOnDevice(id)
            TurnStatus.OFF -> turnOffDevice(id)
        }
    }

    fun updateConfig(id: Long, config: Any): ResponseEntity<HttpStatus> {
        val deviceEntity = deviceRepository.findDeviceByIdAndDeletedIsFalse(id)
        if (deviceEntity.isEmpty) {
            LOGGER.error("Device with id: $id not found")
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
        val device = deviceEntity.get()
        return if (communicationServerFeignClient.updateDeviceConfig(device.uuid!!, config).statusCode.is2xxSuccessful) {
            LOGGER.debug("Updated config for device with id: $id")
            device.status = DeviceStatus.CONNECTED
            deviceRepository.save(device)
            ResponseEntity.ok().build()
        } else {
            LOGGER.error("Error during updating config for device with id: $id")
            ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build()
        }
    }

    private fun turnOnDevice(id: Long): ResponseEntity<HttpStatus> {
        val deviceEntity = deviceRepository.findDeviceByIdAndDeletedIsFalse(id)
        if (deviceEntity.isEmpty) {
            LOGGER.error("Device with id: $id not found")
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
        val device = deviceEntity.get()

        if (device.status!! == DeviceStatus.NEW && device.uuid == null) {
            LOGGER.debug("First connection to device with id: ${device.id} will be established")
            return establishFirstConnection(device)
        }

        val connectionStatus = communicationServerFeignClient.connectDevice(device.uuid!!)
        return if (connectionStatus.statusCode == HttpStatus.ACCEPTED) {
            LOGGER.debug("Connection established with device id: ${device.id}")
            LOGGER.warn("Lack of configuration for device id: ${device.id}")
            device.status = DeviceStatus.CONNECTED_REQ_CONFIG
            deviceRepository.save(device)
            ResponseEntity.status(HttpStatus.ACCEPTED).build()
        } else if (connectionStatus.statusCode.is2xxSuccessful) {
            LOGGER.debug("Connection established with device id: ${device.id}")
            device.status = DeviceStatus.CONNECTED
            deviceRepository.save(device)
            ResponseEntity.ok().build()
        } else {
            LOGGER.error("Connection could not be established with device id: ${device.id}")
            device.status = DeviceStatus.ERROR
            deviceRepository.save(device)
            ResponseEntity.internalServerError().build()
        }
    }

    private fun turnOffDevice(id: Long): ResponseEntity<HttpStatus> {
        val deviceEntity = deviceRepository.findDeviceByIdAndDeletedIsFalse(id)
        if (deviceEntity.isEmpty) {
            LOGGER.error("Device with id: $id not found")
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
        val device = deviceEntity.get()

        return if (DeviceStatus.connectedStatuses.contains(device.status)) {
            if(communicationServerFeignClient.disconnectDevice(device.uuid!!).statusCode.is2xxSuccessful) {
                device.status = DeviceStatus.DISCONNECTED
                deviceRepository.save(device)
                LOGGER.debug("Device with id $id turned off")
                ResponseEntity.ok().build()
            } else {
                LOGGER.error("Error during turning device off - id: $id")
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
            }
        } else {
            LOGGER.warn("Device with id $id wasn't turn on")
            ResponseEntity.status(HttpStatus.ALREADY_REPORTED).build()
        }
    }

    private fun establishFirstConnection(device: Device): ResponseEntity<HttpStatus> {
        val uuid =
            communicationServerFeignClient.establishFirstConnectionToDevice(device.address!!, device.port!!)
        if (uuid.statusCode.is2xxSuccessful) {
            device.apply { this.uuid = uuid.body; this.status = DeviceStatus.CONNECTED_REQ_CONFIG }
            deviceRepository.save(device)
            LOGGER.debug("Added uuid: ${uuid.body} and status: ${DeviceStatus.CONNECTED_REQ_CONFIG} to device")
            return ResponseEntity.status(HttpStatus.ACCEPTED).build()
        }
        return ResponseEntity.internalServerError().build()
    }

}
