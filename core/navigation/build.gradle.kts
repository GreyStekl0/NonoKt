plugins {
    alias(libs.plugins.nonokt.android.library)
}

android {
    namespace = "dev.stekl0.nonokt.core.navigation"
}

dependencies {
    api(libs.androidx.navigation3.runtime)
}
