package com.iot.device

import com.fazecast.jSerialComm.SerialPort
import java.lang.IllegalStateException

fun main() {

    val port = SerialPort.getCommPort("COM3")
    port.setComPortParameters(9600, 8, 1, 0)
    port.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0)

    if (!port.openPort()) {
        throw IllegalStateException("Cant open port")
    }

    val serialPortDataHandler = SerialPortDataHandler()
    port.addDataListener(serialPortDataHandler)


    Runtime.getRuntime().addShutdownHook(Thread(port::closePort))
}

//TODO Logi, próba połączenia po odłączeniu, GRPC?

