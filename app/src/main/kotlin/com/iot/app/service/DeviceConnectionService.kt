package com.iot.app.service

import com.iot.app.domain.entities.Device
import com.iot.app.domain.enums.DeviceStatus
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

    fun connectDevice(id: Long): ResponseEntity<HttpStatus> {
        val deviceEntity = deviceRepository.findDeviceByIdAndDeletedIsFalse(id)
        if(deviceEntity.isEmpty) {
            LOGGER.error("Device with id: $id not found")
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
        val device = deviceEntity.get()
        if(device.status?.equals(DeviceStatus.NEW) == true && device.uuid == null) {
            LOGGER.debug("First connection to device with id: ${device.id} will be established")
            return establishFirstConnection(device)
        }

        val connectionStatus = communicationServerFeignClient.connectDevice(device.uuid!!)
        return if(connectionStatus.statusCode.is2xxSuccessful) {
            device.status = DeviceStatus.CONNECTED
            deviceRepository.save(device)
            LOGGER.debug("Connection established with device id: ${device.id}")
            ResponseEntity.ok().build()
        } else {
            LOGGER.error("Connection could not be established with device id: ${device.id}")
            device.status = DeviceStatus.ERROR
            deviceRepository.save(device)
            ResponseEntity.internalServerError().build()
        }
    }

    fun disconnectDevice() {

    }

    fun fetchStatuses() {

    }

    fun endStatuses() {

    }

    //todo dokończyć razem ze sprawdzaniem statusu
    //todo zamienić pobieranie czasu na localdatetime z zoneoffset
    fun updateConfig(id: Long, config: Any): ResponseEntity<HttpStatus> {
        val deviceEntity = deviceRepository.findDeviceByIdAndDeletedIsFalse(id)
        if(deviceEntity.isEmpty) {
            LOGGER.error("Device with id: $id not found")
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
        val device = deviceEntity.get()
        return communicationServerFeignClient.updateDeviceConfig(device.uuid!!, config)
    }

    private fun establishFirstConnection(device: Device): ResponseEntity<HttpStatus> {
        val uuid =
            communicationServerFeignClient.establishFirstConnectionToDevice(device.address!!, device.port!!)
        if(uuid.statusCode.is2xxSuccessful) {
            device.uuid = uuid.body
            deviceRepository.save(device)
            LOGGER.debug("Added uuid: ${uuid.body} to device")
        }
        return ResponseEntity.ok().build()
    }

    //TODO: przemyśleć ścieżkę nowe -> połączone -> rozłączone -> połączenie ale nie zapisane jednak w bazie - co wtedy


}