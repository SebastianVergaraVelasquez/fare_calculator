plugins {
    kotlin("jvm") version "1.9.24"
    kotlin("plugin.serialization") version "1.9.24"
    id("io.ktor.plugin") version "2.3.12"
    application
}

group = "org.sebastianv"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val ktorVersion = "2.3.12"
val logbackVersion = "1.5.25"

dependencies {
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:${ktorVersion}")
    implementation("io.ktor:ktor-server-config-yaml:$ktorVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("org.jetbrains.exposed:exposed-core:0.44.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.44.0")
    implementation("com.mysql:mysql-connector-j:8.3.0")
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    testImplementation("io.mockk:mockk:1.13.10")
    testImplementation("io.ktor:ktor-server-test-host:${ktorVersion}")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.9.24")
    testImplementation("io.ktor:ktor-client-content-negotiation:${ktorVersion}")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("org.sebastianv.ApplicationKt")
}

kotlin {
    jvmToolchain(21)
}