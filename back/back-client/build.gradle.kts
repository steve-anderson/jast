plugins {
    base
    java
}

base.archivesBaseName = "back-client"

val serverCompileOnly by configurations
val compileOnly: Configuration by configurations
compileOnly.extendsFrom(serverCompileOnly)

val remoteClientCompile by configurations
val compile by configurations
compile.extendsFrom(remoteClientCompile)

dependencies {
    compile(project(":back:back-api"))
}
