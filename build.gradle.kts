import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm") version "1.8.21" // Ensure the Kotlin version is compatible with your JVM target
    id("org.jetbrains.compose") version "1.5.10" // Update to the latest Compose plugin version
}

group = "ke.co.equitybank"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation("org.apache.commons:commons-csv:1.10.0")
}

kotlin {
    jvmToolchain(17) // Align with the desired Java version (22 in this case)
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "LargeCSVReader"
            packageVersion = "1.0.0"
        }
    }
}
