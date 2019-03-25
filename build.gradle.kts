import com.bmuschko.gradle.docker.DockerExtension
import com.bmuschko.gradle.docker.DockerRegistryCredentials

plugins {
    java
    id("com.bmuschko.docker-java-application") version "3.6.2" apply false
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
    docker.registryCredentials {
        url = "https://$dockerRegistryRepo/"
        username = dockerRegistryUser
        password = dockerRegistryPassword
    }

    val baseImplementation by configurations.creating
    val baseRuntime by configurations.creating

    val serverCompileOnly by configurations.creating
    val serverImplementation by configurations.creating
    serverImplementation.extendsFrom(baseImplementation)
    val serverRuntime by configurations.creating
    serverRuntime.extendsFrom(baseRuntime)

    val serverExtraDeployment by configurations.creating

    val remoteClientImplementation by configurations.creating

    dependencies {
        add(baseImplementation.name, "org.slf4j:slf4j-api:1.7.25")

        add(baseRuntime.name, "ch.qos.logback:logback-classic:1.2.3")

        add(serverCompileOnly.name, "javax:javaee-api:7.0")
        add(serverImplementation.name, "com.fasterxml.jackson.core:jackson-databind:2.9.5")

        add(serverExtraDeployment.name, "org.postgresql:postgresql:42.2.4")
        add(serverExtraDeployment.name, "org.apache.activemq:activemq-rar:5.15.8") {
            setTransitive(false)
        }

        add(remoteClientImplementation.name, "org.jboss.spec.javax.ejb:jboss-ejb-api_3.2_spec:1.0.0.Final")
        add(remoteClientImplementation.name, "org.jboss:jboss-remote-naming:2.0.4.Final")
        add(remoteClientImplementation.name, "org.jboss:jboss-ejb-client:2.1.4.Final")
        add(remoteClientImplementation.name, "org.jboss.xnio:xnio-nio:3.3.4.Final")
        add(remoteClientImplementation.name, "org.jboss.marshalling:jboss-marshalling-river:1.4.10.Final")
        add(remoteClientImplementation.name, "org.jboss.spec.javax.transaction:jboss-transaction-api_1.2_spec:1.0.0.Final")
    }
}
