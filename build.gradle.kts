plugins {
    id("java-library")
    id("maven-publish")

    id ("net.minecrell.plugin-yml.bukkit") version "0.5.3"
}

group = "me.danjono"
version = "1.5.3"

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    api("io.papermc.paper:paper-api:1.19.4-R0.1-SNAPSHOT")
}

java {
    withSourcesJar()
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

publishing {
    publications.create<MavenPublication>("maven") {
        artifactId = project.name.lowercase()
        from(components["java"])
    }
}

bukkit {
    main = "me.danjono.inventoryrollback.InventoryRollbackMain"
    authors = listOf("danjono", "booky10")
    apiVersion = "1.19"
}
