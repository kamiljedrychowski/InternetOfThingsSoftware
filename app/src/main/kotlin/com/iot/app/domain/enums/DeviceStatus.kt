package com.iot.app.domain.enums

import java.util.*

enum class DeviceStatus {
    NEW, //nowo dodane
    CONNECTED, //podłączone
    CONNECTED_REQ_CONFIG, //podłączone, ale brakuje konfiguracji
    DISCONNECTED, //dodane - zapisane w bazie communication manager
    ERROR; // jakiś błąd

    companion object {
        val connectedStatuses: EnumSet<DeviceStatus> = EnumSet.of(CONNECTED, CONNECTED_REQ_CONFIG, ERROR)
    }

}
