plugins {
    base
    java
}

base.archivesBaseName = "middle-client"

val serverCompileOnly by configurations
val compileOnly by configurations
compileOnly.extendsFrom(serverCompileOnly)

val remoteClientImplementation by configurations
val implementation by configurations
implementation.extendsFrom(remoteClientImplementation)

dependencies {
    implementation(project(":middle:middle-api"))
}
