import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.1.0"
    `maven-publish`
    signing
}

group = "io.github.reugn"

repositories {
    mavenCentral()
}

java {
    withJavadocJar()
    withSourcesJar()
}

extra["aerospikeClientVersion"] = "9.0.2"
extra["kotlinxCoroutinesVersion"] = "1.7.3"
extra["nettyVersion"] = "4.1.97.Final"

dependencies {
    implementation("com.aerospike:aerospike-client-jdk8:${project.extra["aerospikeClientVersion"]}")
    implementation("io.netty:netty-transport:${project.extra["nettyVersion"]}")
    implementation("io.netty:netty-transport-native-epoll:${project.extra["nettyVersion"]}")
    implementation("io.netty:netty-transport-native-kqueue:${project.extra["nettyVersion"]}")
    implementation("io.netty:netty-handler:${project.extra["nettyVersion"]}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${project.extra["kotlinxCoroutinesVersion"]}")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${project.extra["kotlinxCoroutinesVersion"]}")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<JavaCompile> {
    sourceCompatibility = "1.8"
    targetCompatibility = "1.8"
}

publishing {
    publications {
        create<MavenPublication>("mavenCentral") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()
            from(components["java"])
            pom {
                name.set(project.name)
                description.set("Aerospike Client for Kotlin.")
                url.set("https://github.com/reugn/aerospike-client-kotlin")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("reugn")
                        name.set("reugn")
                        email.set("reugpro@gmail.com")
                        url.set("https://github.com/reugn")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/reugn/aerospike-client-kotlin.git")
                    developerConnection.set("scm:git:ssh://github.com/reugn/aerospike-client-kotlin.git")
                    url.set("https://github.com/reugn/aerospike-client-kotlin")
                }
            }
        }
    }
    repositories {
        maven {
            name = "mavenCentral"
            credentials(PasswordCredentials::class)
            val nexus = "https://s01.oss.sonatype.org/"
            val releasesRepoUrl = uri(nexus + "service/local/staging/deploy/maven2")
            val snapshotsRepoUrl = uri(nexus + "content/repositories/snapshots")
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
        }
    }
}

signing {
    sign(publishing.publications["mavenCentral"])
    useGpgCmd()
    sign(configurations.archives.get())
}

tasks.javadoc {
    if (JavaVersion.current().isJava9Compatible) {
        (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    }
}
