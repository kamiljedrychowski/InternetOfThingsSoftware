package com.iot.statusmanager.service

import com.iot.statusmanager.domain.dto.StatusDto
import com.iot.statusmanager.domain.entities.Status
import com.iot.statusmanager.domain.repositories.StatusRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class StatusService(
    private val statusRepository: StatusRepository
) {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(StatusService::class.java)
    }

    fun saveStatus(status: StatusDto): Status {
        var entity = Status(
            statusDate = status.statusDate,
            statusType = status.statusType,
            deviceUuid = status.deviceUuid,
            details = status.details
        )
        entity = statusRepository.save(entity)
        LOGGER.debug("Status saved: $entity")
        return entity
    }

    fun getAllStatuses(): List<Status>? = statusRepository.findAll()

    fun getAllStatusesByDeviceUuid(deviceUuid: UUID): List<Status>? = statusRepository.findAllByDeviceUuid(deviceUuid)

    fun getLastStatusByDevice(deviceUuid: UUID): Status? = statusRepository.findFirstByDeviceUuidOrderByStatusDateDesc(deviceUuid)

}
