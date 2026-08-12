plugins {
    id("build-jvm")
    alias(libs.plugins.kotlinx.serialization)
}

// 1. Настраиваем конфигурацию для получения файла из другого проекта
val resourcesFromLib by configurations.creating {
    isCanBeResolved = true
    isCanBeConsumed = false
}

dependencies {
    implementation(kotlin("stdlib"))

    resourcesFromLib("${libs.dcompose.get()}:resources@zip")

    implementation("com.otus.otuskotlin.skillGrader.project:jackson-api-v1")
    implementation("com.otus.otuskotlin.skillGrader.project:mappers-api-v1")
    implementation("com.otus.otuskotlin.skillGrader.project:stubs")

    testImplementation(kotlin("test-junit5"))

    testImplementation(libs.logback)
    testImplementation(libs.kermit)

    testImplementation(libs.bundles.kotest)

    testImplementation(libs.testcontainers.core)
    testImplementation(libs.coroutines.core)

    testImplementation(libs.ktor.client.core)
    testImplementation(libs.ktor.client.okhttp)

    testImplementation(libs.kotlinx.serialization.core)
    testImplementation(libs.kotlinx.serialization.json)
}

var severity: String = "MINOR"

tasks {
    withType<Test>().configureEach {
        useJUnitPlatform()
        dependsOn("extractLibResources")
    }
    register<Copy>("extractLibResources") {
        from(resourcesFromLib.elements.map { it.map { file -> zipTree(file) } })
        into(layout.buildDirectory.dir("dcompose"))
    }
}