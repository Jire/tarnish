@file:Suppress("UnstableApiUsage")

rootProject.name = "tarnish"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://repo.runelite.net")
        maven("https://jitpack.io")
    }

    pluginManagement.plugins.apply {
        kotlin("jvm").version("2.1.21")

        id("org.zeroturnaround.gradle.jrebel") version "1.2.1"
        id("com.gradleup.shadow") version "8.3.6"
    }
}

include("game-server")
include("game-client")