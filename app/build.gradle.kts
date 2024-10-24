plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlinSerializer)
    id("kotlin-parcelize")
//    kotlin("kapt")
//    id ("com.google.dagger.hilt.android") version "2.52" apply false
//    alias(libs.plugins.hilt)
//    id("kotlin-kapt")
//    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.example.kotlincompose"
    compileSdk = 34
    
    defaultConfig {
        applicationId = "com.example.kotlincompose"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes.add("META-INF/*.kotlin_module")
        }
    }
}

dependencies {
    
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // APPYX/NAVIGATION
    implementation(libs.appyx.core)
//    implementation(libs.appyx.navigation)
//    implementation(libs.appyx.interaction)
//    api(libs.appyx.backstack)
    
    // implementation(libs.serialization)
    
    // KTOR
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.contentNegotiation)
    implementation(libs.ktor.client.json)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.okhttp)
    
    // VIEW MODEL
    implementation(libs.viewmodel.compose)
    
    implementation(libs.coil)
    
    // COMPOSE MATERIAL
    implementation(libs.compose.material)
    
    // COMPOSE SHIMMER
    implementation(libs.compose.shimmer)

    // HILT
//    implementation(libs.hilt.android)
//    kapt(libs.hilt.compiler) // Use kapt for the Hilt compiler
}
