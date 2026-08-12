plugins {
    id("build-jvm")
    id("maven-publish")
}

//group = "com.otus.otuskotlin.skillGrader"
//version = "0.0.1"
//
//allprojects {
//    repositories {
//        mavenCentral()
//    }
//}
//
//subprojects {
//    group = rootProject.group
//    version = rootProject.version
//}

val resourcesZip = tasks.register<Zip>("resourcesZip") {
    archiveClassifier.set("resources")
    archiveExtension.set("zip")
    from("dcompose")
}

// Добавляем артефакт в стандартную конфигурацию runtime,
// чтобы includeBuild мог его сопоставить при поиске зависимости
configurations {
    runtimeElements {
        outgoing.artifact(resourcesZip)
    }
}

// Публикация
publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()

            artifact(resourcesZip) {
                classifier = "resources"
                extension = "zip"
            }
        }
    }
}