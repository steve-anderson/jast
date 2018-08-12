plugins {
    base
    java
}

base.archivesBaseName = "middle-api"

val serverCompileOnly by configurations
val compileOnly: Configuration by configurations
compileOnly.extendsFrom(serverCompileOnly)

dependencies {
    compile(project(":back:messages"))
}
