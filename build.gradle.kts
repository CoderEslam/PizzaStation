buildscript {

    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.5.3")
        classpath("com.google.gms:google-services:4.3.14")


    }
}

plugins {
    //trick: for the same plugin versions in all sub-modules
    id("com.android.application").version("7.2.2").apply(false)
    id("com.android.library").version("7.2.2").apply(false)
    kotlin("android").version("1.7.10").apply(false)
    kotlin("multiplatform").version("1.7.10").apply(false)
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.1" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
