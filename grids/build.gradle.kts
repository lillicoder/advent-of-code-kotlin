dependencies {
    implementation(project(":io"))
    implementation(project(":math"))
    testImplementation(project(":text"))
    testImplementation(kotlin("test"))

    tasks.test {
        useJUnitPlatform()
    }
}
