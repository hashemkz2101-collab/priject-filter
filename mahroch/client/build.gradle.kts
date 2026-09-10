plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}
android {
    namespace = "com.mahroch.client"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.mahroch.client"
        minSdk = 23
        targetSdk = 35
        versionCode = 3
        versionName = "3.0"
    }
}
dependencies {
    implementation("com.zaneschepke:hevtunnel:1.0.1")
}
kotlin { jvmToolchain(17) }
