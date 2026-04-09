plugins {
    alias(libs.plugins.nonokt.android.library)
    alias(libs.plugins.nonokt.android.library.compose)
}

android {
    namespace = "dev.stekl0.nonokt.core.navigation"
}

dependencies {
    api(libs.androidx.navigation3.runtime)
}
