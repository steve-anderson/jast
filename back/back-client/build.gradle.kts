plugins {
    java
}

base.archivesBaseName = "back-client"

val remoteClientImplementation by configurations
val implementation by configurations
implementation.extendsFrom(remoteClientImplementation)

dependencies {
    implementation(project(":back:back-api"))
}
