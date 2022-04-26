package com.iot.app.domain.entities

import com.iot.app.domain.enums.DeviceStatus
import com.iot.app.domain.enums.DeviceType
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DEVICE")
data class Device(

    @Id
    @GeneratedValue
    @Column(name = "ID")
    var id: Long? = null,

    @Column(name = "CREATION_DATE")
    val createdDate: LocalDateTime = LocalDateTime.now()

    ) {

    @Column(name = "UUID")
    var uuid: UUID? = null

    @Column(name = "MODIFICATION_DATE")
    var modifiedDate: LocalDateTime = LocalDateTime.now()

    @Enumerated(EnumType.STRING)
    @Column(name= "TYPE")
    var type: DeviceType? = null

    @Column(name = "NAME")
    var name: String? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    var status: DeviceStatus? = null

    @Column(name = "DESCRIPTION")
    var description: String? = null

    @Column(name = "ADDRESS")
    var address: String? = null

    @Column(name = "PORT")
    var port: Int? = null

    @Column(name = "DELETED")
    var deleted: Boolean = false

    @PreUpdate
    fun setModificationDate() {
        this.modifiedDate = LocalDateTime.now(ZoneOffset.UTC)
    }

}