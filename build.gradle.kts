plugins {
    kotlin("jvm") version "1.9.21" apply false
    alias(libs.plugins.ktlint.gradle) apply false
}

allprojects {
    group = "com.lillicoder.adventofcode.kotlin"
    version = "1.8.1"
}

subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    configure<JavaPluginExtension> {
        withSourcesJar()
        withJavadocJar()
    }

    configure<org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension> {
        jvmToolchain(21)
    }

    configure<PublishingExtension>() {
        publications {
            register<MavenPublication>("jitpack") {
                from(components["java"])
            }
        }
    }
}
