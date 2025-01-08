dependencies {
    implementation(project(":io"))
    implementation(project(":math"))
    testImplementation(kotlin("test"))

    tasks.test {
        useJUnitPlatform()
    }
}
