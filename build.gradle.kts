plugins {
    // this is necessary to avoid the plugins to be loaded multiple times in each subproject's classloader

    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.jbComposeCompiler) apply false
    alias(libs.plugins.jbComposeMultiplatform) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false

    alias(libs.plugins.kotestMultiplatform) apply false
    alias(libs.plugins.realm.kotlin) apply false
}