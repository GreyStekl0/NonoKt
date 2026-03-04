plugins {
    alias(libs.plugins.nonokt.android.feature.impl)
    alias(libs.plugins.nonokt.android.library.compose)
}

android {
    namespace = "dev.stekl0.nonokt.feature.levels.impl"
    testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
    implementation(projects.feature.levels.api)
    implementation(libs.bundles.flowmvi)
}
