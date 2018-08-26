plugins {
    base
    java
}

base.archivesBaseName = "middle-client"

val serverCompileOnly by configurations
val compileOnly by configurations
compileOnly.extendsFrom(serverCompileOnly)

val remoteClientCompile by configurations
val compile by configurations
compile.extendsFrom(remoteClientCompile)

dependencies {
    compile(project(":middle:middle-api"))
}
