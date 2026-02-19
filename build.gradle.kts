val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm") version "2.3.0"
    id("io.ktor.plugin") version "3.4.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.3.0"
}

group = "com.github.emberlyte"
version = "0.0.1"

application {
    mainClass.set("com.github.emberlyte.ApplicationKt")
}

application {
    mainClass.set("com.github.emberlyte.ApplicationKt")
}

ktor {
    fatJar {
        archiveFileName.set("app.jar")
    }
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation("com.sksamuel.hoplite:hoplite-core:3.0.0.RC1")
    runtimeOnly("com.sksamuel.hoplite:hoplite-yaml:3.0.0.RC1")

    implementation("dev.inmo:tgbotapi-jvm:30.0.2")

    implementation("ch.qos.logback:logback-classic:1.5.32")

    implementation("io.ktor:ktor-client-core:2.3.10")
    implementation("io.ktor:ktor-client-cio:2.3.10")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.10")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.10")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}

