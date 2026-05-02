plugins {
    alias(libs.plugins.nonokt.android.feature)
    alias(libs.plugins.nonokt.android.library.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "dev.stekl0.nonokt.feature.levels"
    testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.model)
    implementation(projects.core.ui)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.timber)
}
