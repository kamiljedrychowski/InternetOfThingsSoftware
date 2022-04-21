package com.iot.communicationserver.grpc

import com.iot.communicationserver.entity.dto.StatusChangeResponseDto
import com.iot.communicationserver.entity.dto.ThermometerConfigDto
import com.iot.communicationserver.feign.StatusManagerFeignClient
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.*

@Service
class DeviceClientManager(private val statusManagerFeignClient: StatusManagerFeignClient) {

    private val clientMap = HashMap<UUID, DeviceClient>()

    companion object {
        private val LOGGER = LoggerFactory.getLogger(DeviceClientManager::class.java)
    }

    fun addNewClient(address: String, port: Int): UUID {
        val deviceClient = DeviceClient(
            ManagedChannelBuilder.forAddress(address, port).usePlaintext().build(), statusManagerFeignClient
        )
        val uuid = runBlocking {
            val statusChangeRequest = deviceClient.statusChangeRequest("ON")
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
        clientMap.remove(uuid)
        LOGGER.debug("Client removed for: $uuid")
    }

    fun changeClientStatus(uuid: UUID, status: String): StatusChangeResponseDto? {
        val client = getClient(uuid) ?: return null
        return runBlocking { return@runBlocking client.statusChangeRequest(status) }
    }

    fun setConfig(uuid: UUID, config: Any): ResponseEntity<HttpStatus> {
        val client = getClient(uuid) ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        val thermometerConfigDto: ThermometerConfigDto =
            config as? ThermometerConfigDto ?: return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        MainScope().launch {
            client.statusRequest(thermometerConfigDto.timeSecondInterval)
        }
        return ResponseEntity.ok().build()
    }


}

