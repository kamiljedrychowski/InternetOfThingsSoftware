package com.iot.device

import com.fazecast.jSerialComm.SerialPort
import com.fazecast.jSerialComm.SerialPortDataListener
import com.fazecast.jSerialComm.SerialPortEvent

class SerialPortDataHandler : SerialPortDataListener {
    private val dataRegex = """\d{2}\.\d{2};\d{2}\.\d{2}""".toRegex()
    private var message = ""

    override fun getListeningEvents(): Int {
        return SerialPort.LISTENING_EVENT_DATA_RECEIVED
    }

    override fun serialEvent(serialPortEvent: SerialPortEvent) {
        if (serialPortEvent.eventType == SerialPort.LISTENING_EVENT_DATA_RECEIVED) {
            val temp: String = String(serialPortEvent.receivedData)
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
            println("Data: $data")
        } else {
            println("Error: Incorrect data!!!")
            return
        }
        println("Processing?! ${System.currentTimeMillis()}")

    }

    private fun validateData(data: String): Boolean {
        return data.length == 11 && dataRegex.matches(data)
    }


}