plugins {
    alias(libs.plugins.nonokt.android.application)
    alias(libs.plugins.nonokt.android.application.compose)
    alias(libs.plugins.nonokt.koin)
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
    implementation(projects.feature.levels.api)
    implementation(projects.feature.levels.impl)

    implementation(projects.core.designsystem)
    implementation(projects.core.navigation)

    implementation(libs.androidx.navigation3.ui)
}
