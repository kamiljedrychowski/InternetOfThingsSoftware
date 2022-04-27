package com.iot.communicationserver.domain.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

@Document
data class DeviceConfiguration(
    @Id val id: ObjectId = ObjectId.get(),
    val creationDate: LocalDateTime = LocalDateTime.now(ZoneOffset.UTC),
    var modificationDate: LocalDateTime,
    @Indexed(unique = true)
    val deviceUuid: UUID,
    var details: Any
) {
    override fun toString(): String {
        return "DeviceConfiguration(id=$id, creationDate=$creationDate, modificationDate=$modificationDate, deviceUuid=$deviceUuid, details=$details)"
    }
}
