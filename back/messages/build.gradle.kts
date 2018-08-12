plugins {
    base
    java
}

base.archivesBaseName = "back-messages"

val baseCompile by configurations
val compile: Configuration by configurations
compile.extendsFrom(baseCompile)
