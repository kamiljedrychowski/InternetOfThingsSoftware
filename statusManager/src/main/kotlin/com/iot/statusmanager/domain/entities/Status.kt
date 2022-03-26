package com.iot.statusmanager.domain.entities

import com.iot.statusmanager.domain.enums.StatusType
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.util.*

@Document
data class Status(
    @Id
    val id: ObjectId = ObjectId.get(),
    val createdDate: LocalDateTime = LocalDateTime.now(),
    val statusDate: LocalDateTime?,
    val statusType: StatusType,
    val deviceUuid: UUID
)
