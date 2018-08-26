plugins {
    base
    java
}

base.archivesBaseName = "back-api"

val baseCompile by configurations
val compile: Configuration by configurations
compile.extendsFrom(baseCompile)
