package com.iot.communicationserver.grpc

import DeviceCommunicationGrpcKt
import StatusChangeRequestMessage
import StatusRequestMessage
import StatusResponseMessage
import com.iot.communicationserver.entity.dto.StatusChangeResponseDto
import com.iot.communicationserver.feign.StatusManagerFeignClient
import com.iot.communicationserver.feign.dto.StatusDto
import com.iot.communicationserver.feign.dto.ThermometerDetails
import com.iot.communicationserver.feign.enums.StatusType
import io.grpc.ManagedChannel
import kotlinx.coroutines.flow.collect
import org.slf4j.LoggerFactory
import java.io.Closeable
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*
import java.util.concurrent.TimeUnit

//TODO: jako serwis a przechowywać tylko stuby?
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
        LOGGER.trace("Received statusResponse: $message")
        statusManagerFeignClient.addStatus(
            StatusDto(
                LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(message.ts), ZoneOffset.UTC
                ),
                StatusType.DETAILS,
                UUID.fromString(message.deviceUuid),
                ThermometerDetails(message.temperature.toFloat(), message.humidity.toFloat())
            )
        )
    }

    override fun close() {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
    }
}
