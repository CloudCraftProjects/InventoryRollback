plugins {
    id("java-library")
    id("maven-publish")

    id ("net.minecrell.plugin-yml.bukkit") version "0.6.0"
}

group = "me.danjono"
version = "1.6.0-SNAPSHOT"

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    api("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")
}

java {
    withSourcesJar()
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
        vendor = JvmVendorSpec.ADOPTIUM
    }
}

publishing {
    publications.create<MavenPublication>("maven") {
        artifactId = project.name.lowercase()
        from(components["java"])
    }
}

tasks {
    withType<JavaCompile> {
        options.encoding = Charsets.UTF_8.name()
        options.compilerArgs.add("-Xlint:unchecked")
        options.compilerArgs.add("-Xlint:deprecation")
    }
}

bukkit {
    main = "me.danjono.inventoryrollback.InventoryRollbackMain"
    authors = listOf("danjono", "booky10")
    apiVersion = "1.19"
}
