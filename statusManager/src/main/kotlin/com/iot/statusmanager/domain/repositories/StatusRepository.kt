package com.iot.statusmanager.domain.repositories

import com.iot.statusmanager.domain.entities.Status
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface StatusRepository : MongoRepository<Status, String> {

    fun findAllByDeviceUuid(deviceUuid: UUID): List<Status>?

    fun findFirstByDeviceUuidOrderByStatusDateDesc(deviceUuid: UUID): Status?
}