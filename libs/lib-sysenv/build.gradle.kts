plugins {
    id("build-kmp")
}

kotlin {
    // Удаляем таргет до настройки
    targets.removeIf { it.name == "macosX64" }

    sourceSets {
        // Доступ к jvmMain без 'by getting'
        jvmMain {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
            }
        }

        // nativeMain уже создан автоматически плагином Kotlin.
        // Он объединяет linuxX64Main и macosArm64Main.
        nativeMain {
            // Если нужно добавить специфичные зависимости для native платформ:
            dependencies { }
        }
    }
}