val kotlin_version: String by project
val logback_version: String by project
val exposed_version: String by project
val h2_version: String by project
val postgres_version: String by project
val flyway_version: String by project
val hikari_version: String by project

plugins {
    kotlin("jvm") version "2.0.0"
    id("io.ktor.plugin") version "2.3.11"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.0"
}

group = "dev.rustybite"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    //Ktor Core
    implementation("io.ktor:ktor-server-core-jvm")
    //Ktor Authentication
    implementation("io.ktor:ktor-server-auth-jvm")
    implementation("io.ktor:ktor-server-auth-jwt-jvm")
    //Ktor Session
    implementation("io.ktor:ktor-server-sessions-jvm")
    //Ktor Content Negotiation & Serialization
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    //Ktor Engine
    implementation("io.ktor:ktor-server-netty-jvm")

    //Exposed
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")

    //Flyway
    implementation("org.flywaydb:flyway-database-postgresql:$flyway_version")

    //Postgresql
    implementation("org.postgresql:postgresql:$postgres_version")

    //Hikari CP
    implementation("com.zaxxer:HikariCP:$hikari_version")

    //Logback
    implementation("ch.qos.logback:logback-classic:$logback_version")

    //Test
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}
