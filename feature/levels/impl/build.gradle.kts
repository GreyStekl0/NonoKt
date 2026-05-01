plugins {
    alias(libs.plugins.nonokt.android.feature.impl)
    alias(libs.plugins.nonokt.android.library.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "dev.stekl0.nonokt.feature.levels.impl"
    testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
    implementation(projects.feature.game.impl)
    implementation(projects.core.ui)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.timber)
}
