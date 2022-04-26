plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    api("org.springframework.boot:spring-boot-starter-data-jpa")
    api("org.springframework.boot:spring-boot-starter-web")
    api("org.postgresql:postgresql")
    api("org.liquibase:liquibase-core:4.8.0")

    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client:3.1.1")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:3.1.1")

}