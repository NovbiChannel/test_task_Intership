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
        kotlinCompilerExtensionVersion = "1.4.3"
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

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation ("androidx.navigation:navigation-runtime-ktx:2.7.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    //Pager
    implementation ("androidx.paging:paging-runtime-ktx:$pagingVersion")
    implementation ("androidx.paging:paging-compose:$pagingVersion")
    // Navigation
    implementation ("androidx.navigation:navigation-compose:$navVersion")
    //Realm
    implementation ("io.realm.kotlin:library-base:$realmVersion")
    implementation ("io.realm.kotlin:library-sync:$realmVersion")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.2")
    //Ktor
    implementation ("io.ktor:ktor-client-core:$kTorVersion")
    implementation ("io.ktor:ktor-client-android:$kTorVersion")
    implementation ("io.ktor:ktor-client-serialization:$kTorVersion")
    implementation ("io.ktor:ktor-client-logging:$kTorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$kTorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$kTorVersion")
    //Coil Image
    implementation ("io.coil-kt:coil-compose:1.4.0")
}