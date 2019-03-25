plugins {
    java
}

base.archivesBaseName = "back-api"

val baseImplementation by configurations
val implementation: Configuration by configurations
implementation.extendsFrom(baseImplementation)
