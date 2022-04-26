package com.iot.app.config

import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling

@Configuration
@EnableScheduling
@EnableFeignClients(basePackages = ["com.iot.app.feign"])
class SpringConfig {

}
