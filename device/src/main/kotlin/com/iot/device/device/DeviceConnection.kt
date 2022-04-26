package com.iot.device.device

import com.fazecast.jSerialComm.SerialPort
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

@Service
class DeviceConnection(private val serialPortDataHandler: SerialPortDataHandler) {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(DeviceConnection::class.java)
    }

    @PostConstruct
    fun connectionPortInitializer() {
        val port = SerialPort.getCommPort("COM3")
        port.setComPortParameters(9600, 8, 1, 0)
        port.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0)

        if (!port.openPort()) {
            throw IllegalStateException("Cannot open port")
        }

        port.addDataListener(serialPortDataHandler)
        Runtime.getRuntime().addShutdownHook(Thread(port::closePort))
        LOGGER.info("Connection to device established")
    }

}
