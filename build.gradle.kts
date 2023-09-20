@Suppress("DSL_SCOPE_VIOLATION")  // needed for the new gradle libs.versions.toml magic
plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotest.multiplatform) apply false
}

allprojects {

    group = "ch.rrte"
    version = "0.1.0"

    repositories {
        mavenLocal()
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    //tasks.withType<KotlinCompile> {
    //    kotlinOptions {
    //        jvmTarget = JavaVersion.VERSION_17.majorVersion
    //    }
    //}

    // Run all JVM tests with JUnit5
    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
    }

}

// Example to configure jvm tests to show passing desktop tests in the build
/*
tasks.named<Test>("desktopTest") {
    useJUnitPlatform()
    filter {
        isFailOnNoMatchingTests = false
    }
    testLogging {
        showExceptions = true
        showStandardStreams = true
        events = setOf(
            org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
            org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
        )
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}

 */

// To delete the top build folder
tasks.withType<Delete>() {
    delete(rootProject.buildDir)
}
