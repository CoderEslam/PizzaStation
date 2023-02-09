plugins {
    id("com.android.application")
    kotlin("android")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
}

android {
    namespace = "com.doubleclick.pizzastation.android"
    compileSdk = 32
    defaultConfig {
        applicationId = "com.doubleclick.pizzastation.android"
        minSdk = 21
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"
//        multiDexEnabled = true
        vectorDrawables.useSupportLibrary = true

    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.0"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    buildFeatures {
        viewBinding = true
    }

}

dependencies {
    implementation(project(":shared"))
    implementation("androidx.compose.ui:ui:1.2.1")
    implementation("androidx.compose.ui:ui-tooling:1.2.1")
    implementation("androidx.compose.ui:ui-tooling-preview:1.2.1")
    implementation("androidx.compose.foundation:foundation:1.2.1")
    implementation("androidx.compose.material:material:1.2.1")
    implementation("androidx.activity:activity-compose:1.5.1")
    implementation("androidx.appcompat:appcompat:1.5.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.airbnb.android:lottie:3.4.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.3")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.google.firebase:firebase-auth-ktx:21.1.0")
    implementation("com.google.firebase:firebase-messaging-ktx:23.1.0")
    implementation("com.google.firebase:firebase-messaging-ktx:23.0.2")
    implementation("com.google.firebase:firebase-auth-ktx:21.1.0")
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation("com.squareup.picasso:picasso:2.8")

    // google map dependencies
    implementation("com.google.android.libraries.places:places:3.0.0")
    implementation("com.google.maps.android:android-maps-utils:2.2.0")
    implementation("com.google.maps:google-maps-services:0.2.9")
    implementation("com.karumi:dexter:6.2.3")

    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:1.0.0-rc03")
    //data-store
    implementation("androidx.datastore:datastore-preferences:1.0.0-rc02")
    implementation("com.github.bumptech.glide:glide:4.14.2")
    implementation("androidx.datastore:datastore-core:1.0.0")
    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")
    implementation("io.reactivex.rxjava2:rxandroid:2.0.1")

    implementation("com.myfatoorah:myfatoorah:2.2.15")
    implementation("com.github.chivorns:smartmaterialspinner:1.5.0")

    implementation("com.iceteck.silicompressorr:silicompressor:2.2.4")

    implementation("io.reactivex.rxjava2:rxandroid:2.0.1")


//    implementation("com.android.support:multidex:2.0.1")

}