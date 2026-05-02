plugins {
    alias(libs.plugins.nonokt.android.library)
    alias(libs.plugins.koin.compiler)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "dev.stekl0.nonokt.core.data"
}

dependencies {
    implementation(libs.androidx.datastore.preferences)
    implementation(projects.core.model)
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.annotations)
    implementation(libs.koin.core)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.timber)
}
