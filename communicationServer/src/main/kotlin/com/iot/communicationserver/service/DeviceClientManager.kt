package com.iot.communicationserver.service

import com.iot.communicationserver.domain.dto.ChangeClientStatusDto
import com.iot.communicationserver.feign.StatusManagerFeignClient
import com.iot.communicationserver.feign.enums.StatusType
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.*

@Service
class DeviceClientManager(
    private val statusManagerFeignClient: StatusManagerFeignClient,
    private val deviceConfigurationService: DeviceConfigurationService
) {

    private val clientMap = HashMap<UUID, DeviceClient>()

    companion object {
        private val LOGGER = LoggerFactory.getLogger(DeviceClientManager::class.java)
    }

    fun addNewClient(address: String, port: Int): UUID {
        val deviceClient = DeviceClient(
            ManagedChannelBuilder.forAddress(address, port).usePlaintext().build(), statusManagerFeignClient
        )
        val uuid = runBlocking {
            val statusChangeRequest = deviceClient.statusChangeRequest(StatusType.ON.name)
            clientMap[statusChangeRequest.deviceUUID] = deviceClient
            return@runBlocking statusChangeRequest.deviceUUID
        }
        LOGGER.debug("Added new device client for: $uuid : $address:$port")
        return uuid
    }

    fun getClient(uuid: UUID): DeviceClient? {
        return clientMap[uuid]
    }

    fun removeClient(uuid: UUID) {
        clientMap.remove(uuid)?.close()
        LOGGER.debug("Client removed for: $uuid")
    }

    fun changeClientStatus(changeClientStatusDto: ChangeClientStatusDto): ResponseEntity<HttpStatus> {
        return when(changeClientStatusDto.newStatus) {
            StatusType.ON.name -> onClientStatus(changeClientStatusDto)
            StatusType.OFF.name -> offClientStatus(changeClientStatusDto)
            else -> ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build()
        }
    }

    private fun offClientStatus(changeClientStatusDto: ChangeClientStatusDto): ResponseEntity<HttpStatus> {
        val client = getClient(changeClientStatusDto.uuid) ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        runBlocking { return@runBlocking client.statusChangeRequest(StatusType.OFF.name) }
        removeClient(changeClientStatusDto.uuid)
        return ResponseEntity.ok().build()
    }

    private fun onClientStatus(changeClientStatusDto: ChangeClientStatusDto): ResponseEntity<HttpStatus> {
        addNewClient(changeClientStatusDto.address!!, changeClientStatusDto.port!!)
        val deviceConfig = deviceConfigurationService.getDeviceConfig(changeClientStatusDto.uuid)
        val client = getClient(changeClientStatusDto.uuid) ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).build()

        return if(deviceConfig == null) {
            ResponseEntity.status(HttpStatus.ACCEPTED).build()
        } else {
            //Parsing data only for thermometer
            val statusInterval: Long = (deviceConfig.details as Map<String, Int>)["timeSecondInterval"]?.toLong()
                ?: return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build()
            GlobalScope.launch {
                client.statusRequest(statusInterval)
            }
            ResponseEntity.ok().build()
        }
    }

    suspend fun setConfig(uuid: UUID, config: Any): ResponseEntity<HttpStatus> {
        val client = getClient(uuid) ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        deviceConfigurationService.addOrUpdateDeviceConfig(uuid, config)

        //Parsing data only for thermometer
        val statusInterval: Long = (config as Map<String, Int>)["timeSecondInterval"]?.toLong()
            ?: return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build()

        client.statusChangeRequest(StatusType.OFF.name)
        GlobalScope.launch {
            client.statusRequest(statusInterval)
        }
        return ResponseEntity.ok().build()
    }

}
