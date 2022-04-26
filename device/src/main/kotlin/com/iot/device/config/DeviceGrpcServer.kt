package com.iot.device.config

import com.iot.device.grpc.DeviceGrpcCommunicationService
import io.grpc.Server
import io.grpc.ServerBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class DeviceGrpcServer(
    @Value("\${deviceServer}")
    private val port: Int,
    private val deviceGrpcCommunicationService: DeviceGrpcCommunicationService
) {

    private val server: Server = ServerBuilder
        .forPort(port)
        .addService(deviceGrpcCommunicationService)
        .build()

    fun start() {
        server.start()
        println("Server started, listening on $port")
        Runtime.getRuntime().addShutdownHook(
            Thread {
                println("*** shutting down gRPC server since JVM is shutting down")
                this@DeviceGrpcServer.stop()
                println("*** server shut down")
            }
        )
    }

    private fun stop() {
        server.shutdown()
    }

}
