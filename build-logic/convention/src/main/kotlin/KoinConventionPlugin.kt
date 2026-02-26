import dev.stekl0.nonokt.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

class KoinConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "io.insert-koin.compiler.plugin")

            dependencies {
                "implementation"(platform(libs.findLibrary("koin-bom").get()))
                "implementation"(libs.findLibrary("koin-annotations").get())
            }

            // Add support for Jvm Module, base on org.jetbrains.kotlin.jvm
            pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
                dependencies {
                    "implementation"(libs.findLibrary("koin-core").get())
                }
            }

            // Add support for Android modules, based on AndroidBasePlugin
            pluginManager.withPlugin("com.android.base") {
                dependencies {
                    "implementation"(libs.findBundle("koin-android").get())
                }
            }
        }
    }
}
