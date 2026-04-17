plugins {
    alias(libs.plugins.nonokt.android.feature.impl)
    alias(libs.plugins.nonokt.android.library.compose)
}

android {
    namespace = "dev.stekl0.nonokt.feature.game.impl"
    testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
    implementation(projects.feature.game.api)
    implementation(projects.core.ui)
    implementation(libs.bundles.flowmvi)
    implementation(libs.kotlinx.collections.immutable)

    testImplementation(libs.junit)
}
