import dev.stekl0.nonokt.libs
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class LintConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "io.gitlab.arturbosch.detekt")
            apply(plugin = "org.jmailen.kotlinter")

            extensions.configure<DetektExtension> {
                parallel = true
                buildUponDefaultConfig = true
                config.setFrom(rootProject.files("detekt.yml"))
            }

            dependencies {
                "detektPlugins"(libs.findLibrary("detekt-koin").get())
                "detektPlugins"(libs.findLibrary("detekt-compose").get())
                "ktlint"(libs.findLibrary("ktlint-compose").get())
            }
        }
    }
}
