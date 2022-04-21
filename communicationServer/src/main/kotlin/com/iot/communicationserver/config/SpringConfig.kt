package com.iot.communicationserver.config

import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Configuration

@Configuration
@EnableFeignClients(basePackages = ["com.iot.communicationserver.feign"])
class SpringConfig {
}
