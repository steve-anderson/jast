import com.bmuschko.gradle.docker.DockerExtension
import com.bmuschko.gradle.docker.DockerRegistryCredentials

buildscript {
    dependencies {
        classpath("com.bmuschko:gradle-docker-plugin:3.6.0")
    }
}

plugins {
    java
    id("com.bmuschko.docker-java-application") version "3.6.0" apply false
}

allprojects {
    group = "net.swahome.jast"
    version = "1.0"

    repositories {
        mavenLocal()
        mavenCentral()
        jcenter()
    }
}

val javaVersion = "1.8"

// should be in ~/.gradle/gradle.properties
val dockerRegistryRepo: String by project
val dockerRegistryUser: String by project
val dockerRegistryPassword: String by project

subprojects {
    apply(plugin = "com.bmuschko.docker-java-application")

    tasks.withType<JavaCompile> {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }

    val docker: DockerExtension by extensions
    docker.registryCredentials = DockerRegistryCredentials()
    with(docker.registryCredentials) {
        url = "https://$dockerRegistryRepo/"
        username = dockerRegistryUser
        password = dockerRegistryPassword
    }

    val baseCompile by configurations.creating
    val baseRuntime by configurations.creating

    val serverCompile by configurations.creating
    serverCompile.extendsFrom(baseCompile)
    val serverCompileOnly by configurations.creating
    val serverRuntime by configurations.creating
    serverRuntime.extendsFrom(baseRuntime)

    dependencies {
        add(baseCompile.name, "org.slf4j:slf4j-api:1.7.25")

        add(baseRuntime.name, "ch.qos.logback:logback-classic:1.2.3")

        add(serverCompileOnly.name, "javax:javaee-api:7.0")
    }
}
