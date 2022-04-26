package com.iot.device.grpc

import DeviceCommunicationGrpcKt
import StatusChangeRequestMessage
import StatusChangeResponseMessage
import StatusRequestMessage
import StatusResponseMessage
import com.iot.device.device.DeviceStatusService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

@Service
class DeviceGrpcCommunicationService(
    private val deviceStatusService: DeviceStatusService,
    @Value("\${deviceUuid}")
    private val deviceUuid: UUID
) : DeviceCommunicationGrpcKt.DeviceCommunicationCoroutineImplBase() {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(DeviceGrpcCommunicationService::class.java)
        private const val OFF = "OFF"
        private const val ERROR = "ERROR"
    }

    private var sendingStatus: Boolean = false

    override suspend fun statusChangeRequest(request: StatusChangeRequestMessage): StatusChangeResponseMessage {
        LOGGER.debug("statusChangeRequest: ${request.status}")
        if (request.status.equals(OFF)) {
            sendingStatus = false
        }
        return StatusChangeResponseMessage
            .newBuilder()
            .setTs(LocalDateTime.now(ZoneOffset.UTC).toEpochSecond(ZoneOffset.UTC))
            .setDeviceUuid(deviceUuid.toString())
            .build()
    }

    override fun statusRequest(request: StatusRequestMessage): Flow<StatusResponseMessage> {
        LOGGER.debug("statusRequest: $request")
        sendingStatus = true
        return prepareStatus(request.timeSecondInterval)
    }

    private fun prepareStatus(intervalSeconds: Long): Flow<StatusResponseMessage> = flow {
        while (sendingStatus) {
            val recentDeviceStatus = deviceStatusService.getRecentDeviceStatus()
            var statusResponse: StatusResponseMessage?
            if (recentDeviceStatus == null) {
                LOGGER.warn("Status not available")
                statusResponse = StatusResponseMessage
                    .newBuilder()
                    .setStatus(ERROR)
                    .setTs(LocalDateTime.now(ZoneOffset.UTC).toEpochSecond(ZoneOffset.UTC))
                    .setDeviceUuid(deviceUuid.toString())
                    .build()
            } else {
                statusResponse = StatusResponseMessage
                    .newBuilder()
                    .setTemperature(recentDeviceStatus.temperature!!)
                    .setHumidity(recentDeviceStatus.humidity!!)
                    .setStatus(recentDeviceStatus.state!!.toString())
                    .setTs(recentDeviceStatus.timestamp!!)
                    .setDeviceUuid(deviceUuid.toString())
                    .build()
            }
            LOGGER.debug("Sending status: $statusResponse")
            emit(
                statusResponse
            )
            delay(intervalSeconds * 1000)
        }
    }
}
