plugins {
    alias(libs.plugins.nonokt.android.library)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "dev.stekl0.nonokt.core.model"
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.runtime)
    implementation(libs.kotlinx.serialization.json)
}
