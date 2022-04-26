package com.iot.app.service

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class DeviceStatusScheduler {

    //TODO schedulerm który:
    // - działa co 5 min
    // - pobiera wszystkie urządzenia ze statusem CONNECTED
    // - pobiera dla nich ostatni status i ewentualnie ustawia ERROR
    // - jeżeli jest ERROR to można urządzenie:
    //  WYŁĄCZYĆ -> DISCONNECTED; (ZRESTARTOWAĆ || WYŁĄCZYĆ I WŁĄCZYĆ) -> ODPOWIEDNI STATUS,

    @Scheduled(fixedDelayString  = "\${deviceStatusSchedulerMilliseconds}")
    fun a() {
        println("sada")
    }

}
