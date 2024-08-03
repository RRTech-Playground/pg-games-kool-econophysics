import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalDistributionDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
    // kotlin multiplatform (jvm + js) setup:
    jvm { }
    jvmToolchain(11)

    js {
        binaries.executable()
        browser {
            @OptIn(ExperimentalDistributionDsl::class)
            distribution(Action {
                outputDirectory.set(File("${rootDir}/dist/js"))
            })
            commonWebpackConfig { // from Template
                //mode = KotlinWebpackConfig.Mode.PRODUCTION
                mode = KotlinWebpackConfig.Mode.DEVELOPMENT
            }
            @OptIn(ExperimentalKotlinGradlePluginApi::class)
            compilerOptions {
                target.set("es2015")
            }
        }
    }

    sourceSets {
        val koolVersion = "0.15.1"
        val lwjglVersion = "3.3.3"
        val physxJniVersion = "2.4.0"

        // JVM target platforms, you can remove entries from the list in case you want to target only a specific platform
        val targetPlatforms = listOf("natives-windows", "natives-linux", "natives-macos", "natives-macos-arm64")
        //val targetPlatforms = listOf("natives-macos-arm64")

        commonMain.dependencies {
            implementation("de.fabmax.kool:kool-core:$koolVersion")
            implementation("de.fabmax.kool:kool-physics:$koolVersion")
            //implementation(libs.kool.core)
            //implementation(libs.kool.physics)

            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
            //implementation(libs.kotlinx.coroutines.core)
        }

        jvmMain.dependencies {
            // add additional jvm-specific dependencies here...

            // add required runtime libraries for lwjgl and physx-jni
            for (platform in targetPlatforms) {
                // lwjgl runtime libs
                runtimeOnly("org.lwjgl:lwjgl:$lwjglVersion:$platform")
                listOf("glfw", "opengl", "jemalloc", "nfd", "stb", "vma", "shaderc").forEach { lib ->
                    runtimeOnly("org.lwjgl:lwjgl-$lib:$lwjglVersion:$platform")
                }

                // physx-jni runtime libs
                runtimeOnly("de.fabmax:physx-jni:$physxJniVersion:$platform")

                // Workaround to get rid of the "GLFW may only be used on the main thread and that thread must be the first thread in the process." java.lang.IllegalStateException on Mac
                // Also add -XstartOnFirstThread to the VM Options
                implementation(libs.gdx.lwjgl3.glfw.awt.macos)
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
            //}
        }

        jsMain.dependencies {
            // add additional js-specific dependencies here...
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

    configurations
        .asSequence()
        .filter { it.name.startsWith("common") || it.name.startsWith("jvm") }
        .map { it.copyRecursive().fileCollection { true } }
        .flatten()
        .distinct()
        .filter { it.exists() }
        .map { if (it.isDirectory) it else zipTree(it) }
        .forEach { from(it) }
    from(layout.buildDirectory.files("classes/kotlin/jvm/main"))

    doLast {
        copy {
            from("$buildDir/libs/${archiveBaseName.get()}-runnable.jar")
            into("${rootDir}/dist/jvm")
        }
    }
}

task("runApp", JavaExec::class) {
    group = "app"

    dependsOn("jvmMainClasses")

    classpath = layout.buildDirectory.files("classes/kotlin/jvm/main")
    configurations
        .filter { it.name.startsWith("common") || it.name.startsWith("jvm") }
        .map { it.copyRecursive().fileCollection { true } }
        .forEach { classpath += it }

    jvmArgs("-XstartOnFirstThread")

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