package com.iot.statusmanager.domain.entities

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.util.*

@Document
data class Status(
    @Id val id: ObjectId = ObjectId.get(),
    val statusDate: LocalDateTime?,
    val statusType: String,
    val deviceUuid: UUID,
    val details: Any
) {
    override fun toString(): String {
        return "Status(id=$id, statusDate=$statusDate, statusType='$statusType', deviceUuid=$deviceUuid, details=$details)"
    }
}
