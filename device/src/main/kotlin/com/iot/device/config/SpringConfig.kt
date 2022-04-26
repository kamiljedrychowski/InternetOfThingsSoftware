package com.iot.device.config

import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct

@Configuration
class SpringConfig(
    private val deviceGrpcServer: DeviceGrpcServer
) {

    @PostConstruct
    fun deviceGrpcServerInitialization() {
        val server = deviceGrpcServer
        server.start()
    }

}
