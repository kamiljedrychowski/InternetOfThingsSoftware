package com.iot.communicationserver.feign.dto

data class ThermometerDetails(
    var temp: Float? = null,
    var humidity: Float? = null,
    var status: String? = null
)
