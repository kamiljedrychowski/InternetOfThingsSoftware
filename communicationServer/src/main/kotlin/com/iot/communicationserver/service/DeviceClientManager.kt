package com.iot.communicationserver.service

import com.iot.communicationserver.domain.dto.StatusChangeResponseDto
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

    fun changeClientStatus(uuid: UUID, status: String): StatusChangeResponseDto? {
        //todo tutaj zrobić rozłączanie itd?!
        when(status) {
            StatusType.ON.name -> onClientStatus(uuid)
            StatusType.OFF.name -> offClientStatus(uuid)
        }

        val client = getClient(uuid) ?: return null
        return runBlocking { return@runBlocking client.statusChangeRequest(status) }
    }

    private fun offClientStatus(uuid: UUID) {
        val client = getClient(uuid) ?: return
        runBlocking { return@runBlocking client.statusChangeRequest(StatusType.OFF.name) }
        removeClient(uuid)
    }

    private fun onClientStatus(uuid: UUID) {
        //todo jak włączamy urządzenie i nie ma go zapisanego w lokalnej mapie to przypał, bo potrzebujemy więcej danych żeby się z nim połączyć
//        addNewClient()
    }

    @SuppressWarnings("unchecked")
    suspend fun setConfig(uuid: UUID, config: Any): ResponseEntity<HttpStatus> {
        val client = getClient(uuid) ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        deviceConfigurationService.addOrUpdateDeviceConfig(uuid, config)

        //Parsing data for thermometer
        val statusInterval: Long = (config as Map<String, Int>)["timeSecondInterval"]?.toLong()
            ?: return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build()

        client.statusChangeRequest(StatusType.OFF.name)
        GlobalScope.launch {
            client.statusRequest(statusInterval)
        }
        return ResponseEntity.ok().build()
    }

}
