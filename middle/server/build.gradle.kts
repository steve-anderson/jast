import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import com.bmuschko.gradle.docker.tasks.image.DockerPushImage

plugins {
    base
    java
    war
    id("com.bmuschko.docker-java-application")
}

base.archivesBaseName = "middle-server"

val serverCompile by configurations
val compile: Configuration by configurations
compile.extendsFrom(serverCompile)

val serverRuntime by configurations
val runtime: Configuration by configurations
runtime.extendsFrom(serverRuntime)

val serverCompileOnly by configurations
val compileOnly: Configuration by configurations
compileOnly.extendsFrom(serverCompileOnly)

dependencies {
    compile(project(":middle:api"))
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

val createDockerImage by tasks.creating {
    dependsOn(copyWar)
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
