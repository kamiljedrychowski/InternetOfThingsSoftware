import com.google.protobuf.gradle.*

buildscript {
    dependencies {
        classpath("com.google.protobuf:protobuf-gradle-plugin:0.8.13")
    }
}

plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    id("com.google.protobuf") version "0.8.18"
}

project.ext["grpcVersion"] = "1.44.0"
project.ext["grpcKotlinVersion"] = "1.2.1"
project.ext["protobufVersion"] = "3.19.4"
project.ext["coroutinesVersion"] = "1.6.0"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    api(kotlin("stdlib"))
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:${project.ext["coroutinesVersion"]}")
    api("io.grpc:grpc-protobuf:${project.ext["grpcVersion"]}")
    api("com.google.protobuf:protobuf-java-util:${project.ext["protobufVersion"]}")
    api("com.google.protobuf:protobuf-kotlin:${project.ext["protobufVersion"]}")
    api("io.grpc:grpc-kotlin-stub:${project.ext["grpcKotlinVersion"]}")
    api("io.grpc:grpc-stub:${project.ext["grpcVersion"]}")
    runtimeOnly("io.grpc:grpc-netty:${project.ext["grpcVersion"]}")

    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client:3.1.1")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:3.1.1")

}

sourceSets {
    main {
        java {
            srcDirs("build/generated/source/proto/main", "src/main/kotlin")
        }
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${project.ext["protobufVersion"]}"
    }

    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${project.ext["grpcVersion"]}"
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:${project.ext["grpcKotlinVersion"]}:jdk7@jar"
        }
    }

    generateProtoTasks {
        all().forEach {
            it.plugins {
                id("grpc")
                id("grpckt")
            }
            it.builtins {
                id("kotlin")
            }
        }
    }
}
