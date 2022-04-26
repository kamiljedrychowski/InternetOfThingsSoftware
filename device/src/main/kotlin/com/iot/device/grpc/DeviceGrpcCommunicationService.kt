package com.iot.device.grpc

import StatusChangeRequestMessage
import StatusChangeResponseMessage
import StatusRequestMessage
import StatusResponseMessage
import com.iot.device.device.DeviceStatusService
import com.iot.device.grpc.enums.StatusChange
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class DeviceGrpcCommunicationService(
    private val deviceStatusService: DeviceStatusService,
    @Value("\${deviceUuid}")
    private val deviceUuid: UUID
) : DeviceCommunicationGrpcKt.DeviceCommunicationCoroutineImplBase() {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(DeviceGrpcCommunicationService::class.java)
    }

    private var sendingStatus: Boolean = false

    override suspend fun statusChangeRequest(request: StatusChangeRequestMessage): StatusChangeResponseMessage {
        if (request.status.equals(StatusChange.OFF.toString())) {
            LOGGER.info("Status was turned off")
            sendingStatus = false
        }
        return StatusChangeResponseMessage
            .newBuilder()
            .setTs(System.currentTimeMillis())
            .setDeviceUuid(deviceUuid.toString())
            .build()
    }

    override fun statusRequest(request: StatusRequestMessage): Flow<StatusResponseMessage> {
        LOGGER.info("GiveStatus method with request: $request")
        sendingStatus = true
        return prepareStatus(request.timeSecondInterval)
    }

    private fun prepareStatus(intervalSeconds: Long): Flow<StatusResponseMessage> = flow {
        while (sendingStatus) {
            val recentDeviceStatus = deviceStatusService.getRecentDeviceStatus()
            if (recentDeviceStatus == null) {
                LOGGER.warn("Status not available")
                delay(intervalSeconds * 1000)
                continue
            }
            val value = StatusResponseMessage
                .newBuilder()
                .setTemperature(recentDeviceStatus.temperature!!)
                .setHumidity(recentDeviceStatus.humidity!!)
                .setStatus(recentDeviceStatus.state!!.toString())
                .setTs(recentDeviceStatus.timestamp!!)
                .setDeviceUuid(deviceUuid.toString())
                .build()
            LOGGER.debug(value.toString())
            emit(
                value
            )
            delay(intervalSeconds * 1000)
        }
    }


}