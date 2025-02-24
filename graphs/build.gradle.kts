dependencies {
    implementation(project(":grids"))
    implementation(project(":io"))
    implementation(project(":math"))
    implementation(project(":text"))
    testImplementation(kotlin("test"))

    tasks.test {
        useJUnitPlatform()
    }
}
