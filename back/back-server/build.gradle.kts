import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import com.bmuschko.gradle.docker.tasks.image.DockerPushImage

plugins {
    java
    war
    id("com.bmuschko.docker-java-application")
}

base.archivesBaseName = "back-server"

val serverImplementation by configurations
val implementation: Configuration by configurations
implementation.extendsFrom(serverImplementation)

val serverRuntime by configurations
val runtime: Configuration by configurations
runtime.extendsFrom(serverRuntime)

val serverCompileOnly by configurations
val compileOnly: Configuration by configurations
compileOnly.extendsFrom(serverCompileOnly)

val serverExtraDeployment by configurations

dependencies {
    implementation(project(":back:back-api"))
}

val dockerRegistryRepo: String by project

// constants
val dockerImageBuildDir = "$buildDir/docker"
val dockerUrl = "https://$dockerRegistryRepo/"
val dockerImageName = "$dockerRegistryRepo/research/jast/${base.archivesBaseName}"

val war by tasks

val copyWar by tasks.creating(Copy::class) {
    dependsOn(war)
    from("$buildDir/libs")
    into("$dockerImageBuildDir/libs")
    rename("(.*)-${project.version}.war", "\$1.war")
}

val copyExtraDeployment by tasks.creating(Copy::class) {
    from(serverExtraDeployment)
    into("$dockerImageBuildDir/libs")
    rename("([^0-9-]+)-rar.*rar", "\$1-rar.rar")
}

val createDockerImage by tasks.creating {
    dependsOn(copyExtraDeployment, copyWar)
}

createDockerImage.doFirst {
    copy {
        from("src/main/docker")
        into(dockerImageBuildDir)
    }
}

val buildDockerImage by tasks.creating(DockerBuildImage::class) {
    dependsOn(createDockerImage)

    inputDir = project.file(dockerImageBuildDir)
    tags = setOf("$dockerImageName:${project.version}")
}

val pushDockerImage by tasks.creating(DockerPushImage::class) {
    dependsOn(buildDockerImage)

    imageName = dockerImageName
    tag = "${project.version}"
}
