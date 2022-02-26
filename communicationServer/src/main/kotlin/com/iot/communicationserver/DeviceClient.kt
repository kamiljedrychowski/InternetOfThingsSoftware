package com.iot.communicationserver

import GreeterGrpcKt
import HelloRequest
import StatusRequestMessage
import StatusStopRequest
import io.grpc.ManagedChannel
import kotlinx.coroutines.flow.collect
import java.io.Closeable
import java.util.concurrent.TimeUnit

class DeviceClient(private val channel: ManagedChannel) : Closeable {
    private val stub: GreeterGrpcKt.GreeterCoroutineStub = GreeterGrpcKt.GreeterCoroutineStub(channel)

    suspend fun greet(name: String) {
        val request = HelloRequest.newBuilder().setName(name).build()
        val response = stub.sayHello(request)
        println("Received: ${response.message}")
    }

    suspend fun stop(reason: String) {
        val request = StatusStopRequest.newBuilder().setReason(reason).build()
        val response = stub.stopStatus(request)
        println("Received last status: $response")
    }

    suspend fun getStatus(text: String, timeInterval: Long) {
        val request = StatusRequestMessage.newBuilder().setText(text).setTimeInterval(timeInterval).build()
        val response = stub.giveStatus(request)
        response.collect { value -> println("Received: $value") }
    }

    override fun close() {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
    }
}
