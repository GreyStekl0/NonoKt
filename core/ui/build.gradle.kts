plugins {
    alias(libs.plugins.nonokt.android.library)
    alias(libs.plugins.nonokt.android.library.compose)
}

android {
    namespace = "dev.stekl0.nonokt.core.ui"
}

dependencies {
    implementation(libs.androidx.compose.material3)
}
