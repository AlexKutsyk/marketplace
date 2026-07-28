dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

pluginManagement {
    includeBuild("../build-plugin")
    plugins {
        id("build-jvm") apply false
        id("build-kmp") apply false
    }
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "project"

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

// Включает вот такую конструкцию
//implementation(projects.m2l5Gradle.sub1.ssub1)
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

//include(":app")
include(":jackson-api-v1")
include(":common")
include(":mappers-api-v1")
include(":stubs")
include(":ktor")
include(":log-api-v1")
include(":biz")
include(":app-common")
include(":repo-common")
include(":repo-inmemory")
include(":repo-pgsqlx4k")
include(":repo-tests")
include(":repo-stubs")