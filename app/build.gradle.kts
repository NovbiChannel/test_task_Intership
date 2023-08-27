plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("io.realm.kotlin")
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "com.example.testtaskintership"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.testtaskintership"
        minSdk = 24
        targetSdk = 33
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.2"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    val pagingVersion = "3.2.0"
    val navVersion = "2.7.1"
    val realmVersion = "1.10.0"
    val kTorVersion = "2.3.3"
    val composeVersion = "1.5.0"

    // AndroidX Core KTX
    implementation("androidx.core:core-ktx:1.10.1")

    // AndroidX Lifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")

    // AndroidX Activity Compose
    implementation("androidx.activity:activity-compose:1.7.2")

    // Jetpack Compose
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.ui:ui-graphics:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")

    // Jetpack Compose Material
    implementation("androidx.compose.material:material:$composeVersion")

    // Jetpack Compose Foundation
    implementation("androidx.compose.foundation:foundation:$composeVersion")

    // Jetpack Navigation
    implementation("androidx.navigation:navigation-runtime-ktx:$navVersion")

    // Unit Testing and AndroidX Test
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // AndroidX Compose Test
    androidTestImplementation(platform("androidx.compose:compose-bom:$composeVersion"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeVersion")
    debugImplementation("androidx.compose.ui:ui-tooling:$composeVersion")
    debugImplementation("androidx.compose.ui:ui-test-manifest:$composeVersion")

    // Jetpack Paging
    implementation("androidx.paging:paging-runtime-ktx:$pagingVersion")
    implementation("androidx.paging:paging-compose:$pagingVersion")

    // Jetpack Navigation Compose
    implementation("androidx.navigation:navigation-compose:$navVersion")

    // Realm
    implementation("io.realm.kotlin:library-base:$realmVersion")
    implementation("io.realm.kotlin:library-sync:$realmVersion")

    // Kotlin Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.2")

    // Ktor
    implementation("io.ktor:ktor-client-core:$kTorVersion")
    implementation("io.ktor:ktor-client-android:$kTorVersion")
    implementation("io.ktor:ktor-client-serialization:$kTorVersion")
    implementation("io.ktor:ktor-client-logging:$kTorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$kTorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$kTorVersion")

    // Coil Image
    implementation("com.google.accompanist:accompanist-coil:0.15.0")
}