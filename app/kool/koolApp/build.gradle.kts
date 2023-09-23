import ch.rrte.pg.games.kool.playground.BuildSettings
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalDistributionDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    //kotlin("multiplatform") version "1.8.22"
    alias(libs.plugins.kotest.multiplatform)
    alias(libs.plugins.kotlin.multiplatform)
}

//repositories {
//    mavenLocal()
//    mavenCentral()
//    maven("https://oss.sonatype.org/content/repositories/snapshots")
//}

kotlin {
    // kotlin multiplatform (jvm + js) setup:
    jvm {
        //jvmToolchain(11)
        jvmToolchain(17)
    }
    js(IR) {
        binaries.executable()
        browser {
            @OptIn(ExperimentalDistributionDsl::class)
            distribution(Action {
                //outputDirectory.set(File("${rootDir}/dist/kool-demo"))
                outputDirectory.set(File("${rootDir}/dist/js"))
            })
            commonWebpackConfig(Action {
                mode = if (BuildSettings.isRelease) {
                    KotlinWebpackConfig.Mode.PRODUCTION
                } else {
                    KotlinWebpackConfig.Mode.DEVELOPMENT
                }
            })
        }
    }

    sourceSets {
        // Choose your kool version:
        //val koolVersion = "0.11.0"              // latest stable version
        val koolVersion = "0.12.1-SNAPSHOT"   // newer but minor breaking changes might occur from time to time

        val commonMain by getting {
            dependencies {
                // add additional kotlin multi-platform dependencies here...

                implementation("de.fabmax.kool:kool-core:$koolVersion")
                implementation("de.fabmax.kool:kool-physics:$koolVersion")
                //implementation(libs.kool.core)
                //implementation(libs.kool.physics)

                //implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.serialization.core)
                implementation(libs.kotlinx.serialization.json)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotest.framework.engine)
            }
        }

        // JVM target platforms, you can remove entries from the list in case you want to target
        // only a specific platform
        //val targetPlatforms = listOf("natives-windows", "natives-linux", "natives-macos")
        val targetPlatforms = listOf("natives-macos")
        val jvmMain by getting {
            dependencies {
                // add additional jvm-specific dependencies here...

                // add required runtime libraries for lwjgl and physx-jni
                for (platform in targetPlatforms) {
                    // lwjgl runtime libs
                    val lwjglVersion = "3.3.2"
                    listOf("glfw", "opengl", "jemalloc", "nfd", "stb", "vma", "shaderc").forEach { lib ->
                        runtimeOnly("org.lwjgl:lwjgl-$lib:$lwjglVersion:$platform")
                    }
                    runtimeOnly("org.lwjgl:lwjgl:$lwjglVersion:$platform")

                    // physx-jni runtime libs - these have to match the physx-jni version used by kool-physics...
                    val physxJniVersion = when (koolVersion) {
                    //val physxJniVersion = when ("0.11.0") {
                        "0.11.0" -> "2.0.5"
                        else -> "2.2.1"
                    }
                    runtimeOnly("de.fabmax:physx-jni:$physxJniVersion:$platform")

                    // Workaround to get rid of the "GLFW may only be used on the main thread and that thread must be the first thread in the process." java.lang.IllegalStateException on Mac
                    // Also add -XstartOnFirstThread to the VM Options
                    //implementation(libs.gdx.lwjgl3.glfw.awt.macos)
                }

                // Following code is from the demos
                //val lwjglNatives = "natives-macos"

                //runtimeOnly(libs.lwjgl)
                //runtimeOnly(libs.lwjgl.glfw)
                //runtimeOnly(libs.lwjgl.jemalloc)
                //runtimeOnly(libs.lwjgl.opengl)
                //runtimeOnly(libs.lwjgl.vma)
                //runtimeOnly(libs.lwjgl.shaderc)
                //runtimeOnly(libs.lwjgl.nfd)
                //runtimeOnly(libs.lwjgl.stb)

                //runtimeOnly(libs.physixjni)

                // use this for CUDA acceleration of physics demos (remove above physxJniRuntime dependency)
                //   requires CUDA enabled physx-jni library in directory kool-demo/libs
                //   grab the library from GitHub releases: https://github.com/fabmax/physx-jni/releases
                //   also make sure to use the same version as used by kool-physics
                //runtimeOnly(libs.physixjni.cuda)
            }
        }

        val jsMain by getting {
            dependencies {
                // add additional js-specific dependencies here...
            }
        }
    }
}

task("runnableJar", Jar::class) {
    dependsOn("jvmJar")

    group = "app"
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    archiveAppendix.set("runnable")
    manifest {
        attributes["Main-Class"] = "LauncherKt"
    }

    val jvmConfig = configurations.getByName("jvmRuntimeClasspath").copyRecursive()
    from(
        jvmConfig.fileCollection { true }.files.map { if (it.isDirectory) it else zipTree(it) },
        files("$buildDir/classes/kotlin/jvm/main")
    )

    doLast {
        copy {
            from("$buildDir/libs/${archiveBaseName.get()}-runnable.jar")
            into("${rootDir}/dist/jvm")
        }
    }
}

task("launch", JavaExec::class) {
    group = "app"
    dependsOn("jvmMainClasses")

    val jvmConfig = configurations.getByName("jvmRuntimeClasspath").copyRecursive()
    classpath = jvmConfig.fileCollection { true } + files("$buildDir/classes/kotlin/jvm/main")
    mainClass.set("LauncherKt")
}

val build by tasks.getting(Task::class) {
    dependsOn("runnableJar")
}

val clean by tasks.getting(Task::class) {
    doLast {
        delete("${rootDir}/dist")
    }
}