package com.iot.communicationserver.config

import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@Configuration
@EnableFeignClients(basePackages = ["com.iot.communicationserver.feign"])
@EnableMongoRepositories("com.iot.communicationserver.domain.repositories")
class SpringConfig {
}
