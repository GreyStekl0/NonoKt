plugins {
    alias(libs.plugins.nonokt.android.feature.impl)
    alias(libs.plugins.nonokt.android.library.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "dev.stekl0.nonokt.feature.game.impl"
    testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.ui)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.timber)

    testImplementation(libs.junit)
}
