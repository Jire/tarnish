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
        kotlin("jvm").version("2.0.0")

        id("org.zeroturnaround.gradle.jrebel") version "1.2.0"
        id("com.github.johnrengelman.shadow") version "8.1.1"
    }
}

include("game-server")
include("game-client")