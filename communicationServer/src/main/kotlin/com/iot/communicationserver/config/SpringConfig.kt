package com.iot.communicationserver.config

import com.iot.communicationserver.DeviceClient
import io.grpc.ManagedChannelBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SpringConfig {

    @Bean
    fun deviceClient(): DeviceClient {
        val port = 50051
        val channel = ManagedChannelBuilder.forAddress("localhost", port).usePlaintext().build()
        return DeviceClient(channel)
    }

}