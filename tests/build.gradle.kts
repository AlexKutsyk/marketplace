plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
}

group = "com.otus.otuskotlin.skillGrader.tests"
version = "0.0.1"

allprojects {
    repositories { mavenCentral() }
}

subprojects {
    group = rootProject.group
    version = rootProject.version
}

tasks {
    arrayOf("build", "clean", "check").forEach {tsk ->
        register(tsk) {
            group = "build"
            dependsOn(subprojects.map {  it.getTasksByName(tsk,false)})
        }
    }

    register("e2eTests") {
        dependsOn(project(":e2e-be").tasks.getByName("check"))
    }
}