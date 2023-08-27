// Top-level build file where you can add configuration options common to all sub-projects/modules.
val kotlinVersion = "1.9.0"
plugins {
    id("com.android.application") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    id ("io.realm.kotlin") version "1.10.0" apply false
}
buildscript {
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.5.21")
    }
}