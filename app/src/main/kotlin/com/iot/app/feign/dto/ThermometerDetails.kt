package com.iot.app.feign.dto

data class ThermometerDetails(
    var temp: Float? = null,
    var humidity: Float? = null
)