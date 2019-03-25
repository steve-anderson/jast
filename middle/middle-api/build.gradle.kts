plugins {
    `java-library`
}

base.archivesBaseName = "middle-api"

dependencies {
    api(project(":back:back-api"))
}
