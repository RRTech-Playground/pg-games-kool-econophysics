//import org.gradle.internal.os.OperatingSystem
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalDistributionDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotestMultiplatform)
}

kotlin {

    //jvm("desktop") { }
    //jvmToolchain(11)

    js {
        binaries.executable()
        browser {
            @OptIn(ExperimentalDistributionDsl::class)
            distribution(Action {
                outputDirectory.set(File("${rootDir}/dist/kool-demo"))
            })
            commonWebpackConfig {
                mode = KotlinWebpackConfig.Mode.DEVELOPMENT
//                mode = if (LocalProperties.get(project).isRelease) {
//                    KotlinWebpackConfig.Mode.PRODUCTION
//                } else {
//                    KotlinWebpackConfig.Mode.DEVELOPMENT
//                }
            }
            @OptIn(ExperimentalKotlinGradlePluginApi::class)
            compilerOptions {
                target.set("es2015")
            }
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kool.core)
            implementation(libs.kool.physics)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.core)
            implementation(libs.kotlinx.serialization.json)
        }

//        val desktopMain by getting // from Demo
//        desktopMain.dependencies {
//            // fixme: force required runtime libraries into IntelliJ module classpath by adding them as implementation
//            //  dependencies.
//            //  Notice that runtimeLibs are only available and added to classpath after first build (or after
//            //  cacheRuntimeLibs task is executed manually) AND the gradle project is re-synced.
//            implementation(fileTree("${projectDir}/runtimeLibs") { include("*.jar") })
//        }
    }
}

//tasks["build"].dependsOn("cacheRuntimeLibs")
//task("cacheRuntimeLibs") {
//    doFirst {
//        val os = OperatingSystem.current()
//        val platformName = when {
//            os.isLinux -> "natives-linux"
//            os.isWindows -> "natives-windows"
//            os.isMacOsX && "arm" in os.nativePrefix -> "natives-macos-arm64"
//            os.isMacOsX -> "natives-macos"
//            else -> ""
//        }
//
//        val runtimeLibs = configurations
//            .filter { it.name == "desktopRuntimeClasspath" }
//            .flatMap { it.copyRecursive().fileCollection { true } }
//            .filter { it.name.endsWith("$platformName.jar") && !it.path.startsWith(projectDir.path) }
//        runtimeLibs
//            .forEach {
//                if (!File("${projectDir}/runtimeLibs/${it.name}").exists()) {
//                    copy {
//                        from(it)
//                        into("${projectDir}/runtimeLibs")
//                    }
//                }
//            }
//        File("${projectDir}/runtimeLibs/").listFiles()
//            ?.filter { existing -> runtimeLibs.none { existing.name == it.name } }
//            ?.forEach { it.delete() }
//    }
//}

//tasks["clean"].doLast {
//    delete("${rootDir}/dist/kool-demo")
//    delete("${projectDir}/runtimeLibs")
//}

//tasks.register<JavaExec>("run") {
//    group = "application"
//    mainClass.set("de.fabmax.kool.demo.MainKt")
//
//    kotlin {
//        val main = targets["desktop"].compilations["main"]
//        dependsOn(main.compileAllTaskName)
//        classpath(
//            { main.output.allOutputs.files },
//            { configurations["desktopRuntimeClasspath"] }
//        )
//    }
//}