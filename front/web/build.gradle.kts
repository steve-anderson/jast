import com.bmuschko.gradle.docker.DockerRegistryCredentials
import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import com.bmuschko.gradle.docker.tasks.image.DockerPushImage

plugins {
    base
    id("com.bmuschko.docker-java-application")
}

base.archivesBaseName = "front-web"
/*
val dockerRegistryRepo: String by project

// constants
val dockerImageBuildDir = "$buildDir/docker"
val dockerUrl = "https://$dockerRegistryRepo/"
val dockerImageName = "$dockerRegistryRepo/research/jast/${base.archivesBaseName}"

val copyHtml by tasks.creating(Copy::class) {
    from("src/main/html")
    into("$dockerImageBuildDir/docroot")
}

val build by tasks
build.dependsOn(copyHtml)

val createDockerImage by tasks.creating {
    dependsOn(build)
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
*/

