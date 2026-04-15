plugins {
    alias(libs.plugins.nonokt.android.application)
    alias(libs.plugins.nonokt.android.application.compose)
    alias(libs.plugins.nonokt.koin)
    alias(libs.plugins.hotswan.compiler)
}

android {
    namespace = "dev.stekl0.nonokt"

    defaultConfig {
        applicationId = "dev.stekl0.nonokt"
        versionCode = 1
        versionName = "1.0.0" // X.Y.Z; X = Major, Y = minor, Z = Patch level

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
        }
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
}

dependencies {
    implementation(libs.androidx.compose.material3)
    implementation(projects.core.navigation)
    implementation(projects.feature.game.api)
    implementation(projects.feature.game.impl)
    implementation(projects.feature.levels.api)
    implementation(projects.feature.levels.impl)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.timber)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
