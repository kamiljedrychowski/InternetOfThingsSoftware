package com.iot.communicationserver.service

import DeviceCommunicationGrpcKt
import StatusChangeRequestMessage
import StatusRequestMessage
import StatusResponseMessage
import com.iot.communicationserver.domain.dto.StatusChangeResponseDto
import com.iot.communicationserver.feign.StatusManagerFeignClient
import com.iot.communicationserver.feign.dto.GeneralDetails
import com.iot.communicationserver.feign.dto.StatusDto
import com.iot.communicationserver.feign.dto.ThermometerDetails
import com.iot.communicationserver.feign.enums.StatusType
import io.grpc.ManagedChannel
import org.slf4j.LoggerFactory
import java.io.Closeable
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*
import java.util.concurrent.TimeUnit

//TODO: As a service? store only stubs?
class DeviceClient(
    private val channel: ManagedChannel, private val statusManagerFeignClient: StatusManagerFeignClient
) : Closeable {
    private val stub: DeviceCommunicationGrpcKt.DeviceCommunicationCoroutineStub =
        DeviceCommunicationGrpcKt.DeviceCommunicationCoroutineStub(channel)

    companion object {
        private val LOGGER = LoggerFactory.getLogger(DeviceClient::class.java)
    }

    suspend fun statusChangeRequest(status: String): StatusChangeResponseDto {
        val request =
            StatusChangeRequestMessage.newBuilder().setTs(System.currentTimeMillis()).setStatus(status).build()

        val response = stub.statusChangeRequest(request)
        LOGGER.debug("Received statusChangeResponse: $response")
        return StatusChangeResponseDto(response.ts, UUID.fromString(response.deviceUuid))
    }

    suspend fun statusRequest(secondInterval: Long) {
        val request =
            StatusRequestMessage.newBuilder().setTs(System.currentTimeMillis()).setTimeSecondInterval(secondInterval)
                .build()
        val response = stub.statusRequest(request)
        response.collect { value -> processStatusMessage(value) }
    }

    private fun processStatusMessage(message: StatusResponseMessage) {
        LOGGER.debug("Received statusResponse: $message")
        statusManagerFeignClient.addStatus(
            StatusDto(
                LocalDateTime.now(ZoneOffset.UTC),
                StatusType.THERMOMETER_DETAILS.toString(),
                UUID.fromString(message.deviceUuid),
                if (message.status != "ERROR") ThermometerDetails(
                    message.temperature.toFloat(),
                    message.humidity.toFloat(),
                    message.status
                ) else GeneralDetails(message.status, null)
            )
        )
    }

    override fun close() {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
    }
}
