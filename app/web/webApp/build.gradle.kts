import ch.rrte.pg.games.kool.playground.ProjectConfig

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.kotest.multiplatform)
    alias(libs.plugins.kotlin.multiplatform)
}

kotlin {

    js(IR) {
        browser {
            commonWebpackConfig {
                cssSupport {
                    enabled.set(true)  // Beginning with 1.8.0:
                }
            }
        }
        binaries.executable()
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

        val jsMain by getting {
            dependencies {
                implementation(compose.web.core)
            }
        }
        val jsTest by getting
    }
}

compose.experimental {
    web.application {}
}
