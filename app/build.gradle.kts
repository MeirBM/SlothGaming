plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")
    id("kotlin-kapt")
    id("org.jetbrains.kotlin.plugin.parcelize")
    id ("com.google.dagger.hilt.android")
    id("androidx.navigation.safeargs.kotlin")

}

android {
    namespace = "com.example.SlothGaming"
    compileSdk {
        version = release(36)
    }
    buildFeatures{
        viewBinding = true
    }
    defaultConfig {
        applicationId = "com.example.SlothGaming"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}



dependencies {
    implementation(libs.androidx.navigation.fragment)
    val roomVersion = "2.8.4"
    kapt("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-runtime:${roomVersion}")
    implementation("com.github.bumptech.glide:glide:5.0.5")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.10.0")
    //coroutines dependencies
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.10.2")
    //fire base dependencies
    implementation(platform("com.google.firebase:firebase-bom:34.9.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
    //Hilt
    implementation("com.google.dagger:hilt-android:2.52")
    kapt("com.google.dagger:hilt-compiler:2.52")
    //Retrofit
    // retrofit core
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // Json to Object
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // TO simple String
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")
    //args nav
    implementation("androidx.navigation:navigation-fragment-ktx:$2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:$2.7.7")

}

