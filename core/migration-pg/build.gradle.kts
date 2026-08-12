import com.otus.otuskotlin.skillGrader.plugin.DockerBuildTask
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.registering
import org.gradle.kotlin.dsl.repositories
import org.testcontainers.containers.ComposeContainer

plugins {
    id("build-docker")
}

docker {
    images.register("Pg") {
        buildContext = project.layout.buildDirectory.dir("docker").get().toString()
        imageName = project.name
        imageTag = "${project.version}"
    }
}

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        // Testcontainers core + Docker Compose модуль
        // classpath("org.testcontainers:testcontainers:1.20.6")
        classpath(libs.testcontainers.core)
    }
}

group = "com.otus.otuskotlin.skillGrader.migration"
version = "0.1.0"

val pgContainer: ComposeContainer by lazy {
    ComposeContainer(
        file("src/test/compose/docker-compose-pg.yml")
    )
        .withExposedService("psql", 5432)
}

tasks {
    val buildImages by registering {
        dependsOn("dockerBuildPg")
    }

    val pgDn by registering {
        group = "db"
        doFirst {
            println("Stopping PostgreSQL...")
            pgContainer.stop()
            println("PostgreSQL stopped")
        }
    }
    val pgUp by registering {
        group = "db"
        doFirst {
            println("Starting PostgreSQL...")
            pgContainer.start()
            println("PostgreSQL started at port: ${pgContainer.getServicePort("psql", 5432)}")
        }
        finalizedBy(pgDn)
    }
}

afterEvaluate {
    tasks {
        named("dockerBuildPg", DockerBuildTask::class) {
            doFirst {
                copy {
                    from("src/main/liquibase") //{ into("${buildContext.get()}/liquibase") }
                    from("src/main/docker/Dockerfile")
                    into(buildContext)
                }
            }
        }
    }
}