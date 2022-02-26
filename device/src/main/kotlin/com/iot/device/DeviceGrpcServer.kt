package com.iot.device

import HelloReply
import HelloRequest
import StatusRequestMessage
import StatusResponseMessage
import StatusStopRequest
import io.grpc.Server
import io.grpc.ServerBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class DeviceGrpcServer(private val port: Int) {
    private val server: Server = ServerBuilder
        .forPort(port)
        .addService(HelloWorldService())
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

    fun blockUntilShutdown() {
        server.awaitTermination()
    }

    private class HelloWorldService : GreeterGrpcKt.GreeterCoroutineImplBase() {

        val log: Logger = LoggerFactory.getLogger(this.javaClass)
        var sendStatus: Boolean = false

        override suspend fun sayHello(request: HelloRequest): HelloReply {
            log.info("sayHello method with request: $request")
            return HelloReply
                .newBuilder()
                .setMessage("Hello ${request.name}")
                .build()
        }

        override suspend fun stopStatus(request: StatusStopRequest): StatusResponseMessage {
            log.info("stopStatus method with request: $request")
            sendStatus = false
            return StatusResponseMessage
                .newBuilder()
                .setStatus("Status message")
                .setTemperature(-1L)
                .build()
        }


        override fun giveStatus(request: StatusRequestMessage): Flow<StatusResponseMessage> {
            log.info("giveStatus method with request: $request")
            sendStatus = true
            return prepareStatus(request.timeInterval)
        }


        fun prepareStatus(intervalSeconds: Long): Flow<StatusResponseMessage> = flow {
            var i = -5
            while (sendStatus) {
                emit(
                    StatusResponseMessage.newBuilder().setStatus("Status message").setTemperature(i++.toLong()).build()
                )
                delay(intervalSeconds)
            }
        }
    }
}
