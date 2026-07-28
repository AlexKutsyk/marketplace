rootProject.name = "tests"

dependencyResolutionManagement {
    repositories {
        maven { url = uri("${rootProject.projectDir}/../core/build/repo") }
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

pluginManagement {
    includeBuild("../build-plugin")
    plugins {
        id("build-jvm").apply(false)
        id("build-kmp").apply(false)
    }
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

include(":e2e-be")