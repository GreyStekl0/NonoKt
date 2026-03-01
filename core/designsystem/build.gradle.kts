plugins {
    alias(libs.plugins.nonokt.android.library)
    alias(libs.plugins.nonokt.android.library.compose)
}

android {
    namespace = "dev.stekl0.nonokt.core.designsystem"
    testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
    api(libs.androidx.compose.material3)
}
