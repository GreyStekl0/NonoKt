plugins {
    alias(libs.plugins.nonokt.android.feature.impl)
    alias(libs.plugins.nonokt.android.library.compose)
}

android {
    namespace = "dev.stekl0.nonokt.feature.game.impl"
    testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
    implementation(libs.bundles.flowmvi)
}
