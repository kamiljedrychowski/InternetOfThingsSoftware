package com.iot.device.device

import com.fazecast.jSerialComm.SerialPort
import com.fazecast.jSerialComm.SerialPortDataListener
import com.fazecast.jSerialComm.SerialPortEvent
import com.iot.device.device.dto.DeviceStatus
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class SerialPortDataHandler (private val deviceStatusService: DeviceStatusService): SerialPortDataListener {
    private val dataRegex = """\d{2}\.\d{2};\d{2}\.\d{2}""".toRegex()
    private var message = ""

    companion object{
        private val LOGGER = LoggerFactory.getLogger(SerialPortDataHandler::class.java)
    }

    override fun getListeningEvents(): Int {
        return SerialPort.LISTENING_EVENT_DATA_RECEIVED
    }

    override fun serialEvent(serialPortEvent: SerialPortEvent) {
        if (serialPortEvent.eventType == SerialPort.LISTENING_EVENT_DATA_RECEIVED) {
            val temp = String(serialPortEvent.receivedData)
            if (temp.contains("\n")) {
                message += temp
                processEventData(message.trim())
                message = ""
            } else {
                message += temp
            }
        }
    }

    private fun processEventData(data: String) {
        if (validateData(data)) {
            LOGGER.trace("Data: $data")
        } else {
            LOGGER.warn("Error: Incorrect data!!!")
            return
        }
        LOGGER.trace("Processing at ${System.currentTimeMillis()}")
        deviceStatusService.updateDeviceStatus(DeviceStatus(data).translateData())
    }

    private fun validateData(data: String): Boolean {
        return data.length == 11 && dataRegex.matches(data)
    }


}