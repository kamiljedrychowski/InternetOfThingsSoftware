pluginManagement {
    repositories {
        maven { url = uri("https://repo.spring.io/milestone") }
        maven { url = uri("https://repo.spring.io/snapshot") }
        gradlePluginPortal()
    }
}

rootProject.name = "InternetOfThingsSoftware"

include ("app")
include ("communicationServer")
include ("device")
include ("statusManager")
