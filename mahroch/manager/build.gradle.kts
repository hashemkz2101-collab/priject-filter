plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}
android {
    namespace = "com.mahroch.manager"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.mahroch.manager"
        minSdk = 23
        targetSdk = 35
        versionCode = 3
        versionName = "3.0"
    }
}
kotlin { jvmToolchain(17) }
