dependencies {
    testImplementation(project(":text"))
    testImplementation(kotlin("test"))

    tasks.test {
        useJUnitPlatform()
    }
}
