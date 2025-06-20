import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.example.dagger2"
    compileSdk = 35

    buildFeatures {
        buildConfig = true
    }
    defaultConfig {
        applicationId = "com.example.dagger2"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val keystoreFile = project.rootProject.file("local.properties")
        val properties = Properties()
        properties.load(keystoreFile.inputStream())

        val token = properties.getProperty("token") ?: ""
        val baseUrl = properties.getProperty("baseUrl") ?: ""
        buildConfigField(
            type = "String",
            name = "baseUrl",
            value = baseUrl
        )
        buildConfigField(
            type = "String",
            name = "token",
            value = token
        )
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
    viewBinding {
        enable = true
    }
}

dependencies {
    // Glide
    implementation(libs.glide)
    // Lifecycle
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.livedata)
    // Internet
    implementation(libs.okhttp)
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson.converter)
    // Dagger2
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.google.play.services.location)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}