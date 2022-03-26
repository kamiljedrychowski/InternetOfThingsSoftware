package com.iot.statusmanager.domain.repositories

import com.iot.statusmanager.domain.entities.Status
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface StatusRepository : MongoRepository<Status, String> {

}