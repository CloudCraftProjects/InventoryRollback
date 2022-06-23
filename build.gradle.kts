plugins {
    id("java-library")
    id("maven-publish")
}

group = "me.danjono"
version = "1.5.2"

repositories {
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    api("io.papermc.paper:paper-api:1.19-R0.1-SNAPSHOT")
}

java {
    withSourcesJar()
}

publishing {
    publications.create<MavenPublication>("maven") {
        artifactId = project.name.toLowerCase()
        from(components["java"])
    }
}

tasks.processResources {
    inputs.property("version", project.version)
    filesMatching("plugin.yml") {
        expand("version" to project.version)
    }
}
