import org.gradle.kotlin.dsl.named
import com.otus.otuskotlin.skillGrader.plugin.DockerBuildTask
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.targets.native.tasks.KotlinNativeTest
import org.testcontainers.containers.ComposeContainer
import org.testcontainers.containers.wait.strategy.Wait
import java.time.Duration

// ============================================================
// Testcontainers в buildscript — это Gradle-уровень.
// Контейнер стартует не из тестового класса, а из задач Gradle.
// Тесты получают порт через system property / env.
// ============================================================
buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath(libs.testcontainers.core)
    }
}

plugins {
    alias(libs.plugins.kotlinx.serialization)
    id("build-kmp")
    alias(libs.plugins.shadowJar)
    id("build-docker")
}

docker {
    // JVM образ
    images.register("Jvm") {
        buildContext = project.layout.buildDirectory.dir("docker-jvm").get().toString()
        dockerFile = "Dockerfile"
        dependsOnTask = "jvmJar"
        imageName = "${project.name}-jvm"
        imageTag = "${project.version}"
    }
}

kotlin {
    jvm {  }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation(libs.ktor.server.core)
//                implementation(libs.ktor.server.cio)
                implementation(libs.ktor.server.cors)
                implementation(libs.ktor.server.yaml)
                implementation(libs.ktor.server.negotiation)
                implementation(libs.ktor.server.headers.response)
                implementation(libs.ktor.server.headers.caching)
                implementation(libs.ktor.server.websocket)

//                // Для того, чтоб получать содержимое запроса более одного раза
//                В Application.main добавить `install(DoubleReceive)`
//                implementation("io.ktor:ktor-server-double-receive:${libs.versions.ktor.get()}")

                implementation(project(":common"))
                implementation(project(":app-common"))
                implementation(project(":biz"))

                // Stubs
                implementation(project(":stubs"))

                implementation(libs.kotlinx.serialization.core)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.ktor.serialization.json)

                // logging
                implementation(project(":log-api-v1"))
                implementation("com.otus.otuskotlin.skillGrader.libs:lib-logging-common")
                implementation("com.otus.otuskotlin.skillGrader.libs:lib-logging-kermit")
                implementation("com.otus.otuskotlin.skillGrader.libs:lib-logging-socket")

                // DB
                implementation(libs.uuid)
                implementation(projects.repoCommon)
                implementation(projects.repoStubs)
                implementation(projects.repoInmemory)

                /**
                 * Репозиторий PostgreSQL — мультиплатформенный.
                 * Доступен для JVM, linuxX64 и macosArm64.
                 */
                implementation(projects.repoPgsqlx4k)

                // logging
                implementation(project(":log-api-v1"))
                implementation(libs.self.lib.logs.common)
                implementation(libs.self.lib.logs.kermit)
                implementation(libs.self.lib.logs.socket)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))

                implementation(libs.ktor.server.test)
                implementation(libs.ktor.client.negotiation)

                // DB
                implementation(projects.repoCommon)

                // Чтение переменных окружения (JVM + Native)
                implementation(libs.self.lib.sysenv)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))

                // jackson
                implementation(libs.ktor.serialization.jackson)
                implementation(libs.ktor.server.calllogging)
                implementation(libs.ktor.server.headers.default)

                implementation(libs.logback)

                // ktor
                implementation(libs.ktor.server.netty)

                // transport models
                implementation(project(":jackson-api-v1"))
                implementation(project(":mappers-api-v1"))
                implementation("com.otus.otuskotlin.skillGrader.libs:lib-logging-logback")
                implementation(libs.testcontainers.postgres)
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation(project(":jackson-api-v1"))
            }
        }
    }
}

// ============================================================
//  PostgreSQL через Testcontainers (Gradle-level)
//  Используем ComposeContainer — он поднимает psql + liquibase
//  из docker-compose-pg.yml, который лежит в тестовых ресурсах.
// ============================================================
val PG_SERVICE = "psql"
val MG_SERVICE = "liquibase"

val pgContainer: ComposeContainer by lazy {
    val res = objects.fileCollection()
        .from("src/jvmTest/resources/docker-compose-pg.yml")
        .singleFile
    ComposeContainer(res)
        .withExposedService(PG_SERVICE, 5432)
        .withStartupTimeout(Duration.ofSeconds(300))
        .waitingFor(
            MG_SERVICE,
            Wait.forLogMessage(".*Liquibase command 'update' was executed successfully.*", 1)
        )
}

val pgUp by tasks.registering {
    doFirst {
        println("Starting PostgreSQL container...")
        pgContainer.start()
        println("PostgreSQL started at port: ${pgContainer.getServicePort(PG_SERVICE, 5432)}")
    }
    finalizedBy(pgDn)
}

val pgDn by tasks.registering {
    doFirst {
        println("Stopping PostgreSQL container...")
        pgContainer.stop()
    }
}

// ============================================================
//  Настройка JVM-тестов
// ============================================================
/**
 * jvmTest — обычные тесты, НЕ требующие PostgreSQL.
 * PG-тесты исключены через фильтр.
 */
tasks.named<Test>("jvmTest") {
    filter.excludeTestsMatching("*RuleRepoPGTest*")
}

/**
 * jvmTestPg — только PG-тесты, перед ними стартует Docker-контейнер.
 * Используем тот же testClassesDirs и classpath, что и jvmTest,
 * но с противоположным фильтром.
 */
val jvmTestPg by tasks.registering(Test::class) {
    group = "verification"
    description = "Запускает только PG-тесты с поднятием Docker-контейнера (psql + liquibase)"

    dependsOn(pgUp)
    finalizedBy(pgDn)

    filter.includeTestsMatching("*RuleRepoPGTest*")

    testClassesDirs = sourceSets["jvmTest"].output.classesDirs
    classpath = sourceSets["jvmTest"].runtimeClasspath

    // Передаём параметры PostgreSQL в тесты через environment (единый API для JVM и native)
    doFirst {
        val pgPort = pgContainer.getServicePort(PG_SERVICE, 5432)
        environment("postgresHost", "localhost")
        environment("postgresPort", pgPort.toString())
        environment("postgresUser", "postgres")
        environment("postgresPass", "skill-grader-pass")
    }

    useJUnit()
}

// ============================================================
//  Настройка Native-тестов (linuxX64, macosArm64)
//
//  Без флага -PwithPg: PG-тесты исключены, контейнер не стартует.
//  С флагом -PwithPg:   PG-тесты запускаются, перед ними стартует Docker.
//  Порт PostgreSQL передаётся в тест через env (getenv на native).
// ============================================================
tasks.withType<KotlinNativeTest>().configureEach {
    if (!project.hasProperty("withPg")) {
        filter.excludeTestsMatching("*RuleRepoPGTest*")
    } else {
        dependsOn(pgUp)
        finalizedBy(pgDn)
        doFirst {
            val pgPort = pgContainer.getServicePort(PG_SERVICE, 5432)
            environment("postgresHost", "localhost")
            environment("postgresPort", pgPort.toString())
            environment("postgresUser", "postgres")
        environment("postgresPass", "skill-grader-pass")
        }
    }
}

// ============================================================
//  check — запускает все тесты: обычные + PG
// ============================================================
tasks.named("check") {
    dependsOn(jvmTestPg)
}

tasks {
    // Если ошибка: "Entry application.yaml is a duplicate but no duplicate handling strategy has been set."
    // Возникает из-за наличия файлов как в common, так и в jvm платформе
    withType(ProcessResources::class) {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }


    named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
        isZip64 = true
        manifest {
            // Optionally, set the main class for the shadowed JAR.
            attributes["Main-Class"] = "io.ktor.server.cio.EngineMain"
        }
        dependencies {
            exclude(dependency("org.graalvm.js:js:.*"))
            exclude(dependency("org.graalvm.polyglot:js:.*"))
        }
        // Исключаем проблемные файлы из упаковки
        exclude("**/*.pom")
        exclude("**/*.module")
    }
}

afterEvaluate {
    tasks {
        named("dockerBuildJvm", DockerBuildTask::class) {
            dependsOn(shadowJar)
            group = "docker"
            doFirst {
                copy {
                    from("Dockerfile.jvm") { rename { "Dockerfile" } }
                    from(shadowJar.get().archiveFile.get())
                    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
                    println("BUILD CONTEXT: ${buildContext.get()}")
                    into(buildContext)
                }
            }
        }
    }
}