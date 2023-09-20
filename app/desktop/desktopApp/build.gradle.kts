import ch.rrte.pg.games.kool.playground.ProjectConfig

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.kotest.multiplatform)
    alias(libs.plugins.kotlin.multiplatform)
}

kotlin {

    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = "17"
        }
        withJava()
    }

    targets.all {
        compilations.all {
            kotlinOptions {
                verbose = true
            }
        }
    }

    sourceSets {

        val commonMain by getting {
            dependencies {
                implementation(project(ProjectConfig.modules.coreUi))

                implementation(compose.foundation)
                implementation(compose.runtime)
                implementation(compose.ui)

                implementation(libs.koin.core)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotest.framework.engine)
            }
        }

        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
        val desktopTest by getting {
            dependencies {
                implementation(libs.kotest.runner.junit5)
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "ch.rrte.pg.games.kool.playground.app.desktop.RRTechDesktopAppKt"

        nativeDistributions {
            targetFormats(
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Dmg,
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Msi,
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Deb
            )
            packageName = "RRTechDesktopApp"
            packageVersion = "1.0.0"
        }
    }
}
