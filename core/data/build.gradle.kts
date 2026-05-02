plugins {
    alias(libs.plugins.nonokt.android.library)
    alias(libs.plugins.koin.compiler)
}

android {
    namespace = "dev.stekl0.nonokt.core.data"
}

dependencies {
    implementation(libs.androidx.datastore.preferences)
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.annotations)
    implementation(libs.koin.core)
    implementation(libs.kotlinx.collections.immutable)
}
