import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.31"
}

group = "io.github.reugn"
version = "0.1.0"

repositories {
    mavenCentral()
}

extra["aerospikeClientVersion"] = "5.1.8"
extra["kotlinxCoroutinesVersion"] = "1.5.2"
extra["nettyVersion"] = "4.1.69.Final"

dependencies {
    implementation("com.aerospike:aerospike-client:${project.extra["aerospikeClientVersion"]}")
    implementation("io.netty:netty-all:${project.extra["nettyVersion"]}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${project.extra["kotlinxCoroutinesVersion"]}")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${project.extra["kotlinxCoroutinesVersion"]}")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}
