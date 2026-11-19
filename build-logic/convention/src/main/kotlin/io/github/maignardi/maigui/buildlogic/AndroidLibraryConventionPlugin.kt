package io.github.maignardi.maigui.buildlogic

import com.android.build.api.dsl.LibraryExtension
import kotlinx.validation.KotlinApiBuildTask
import kotlinx.validation.KotlinApiCompareTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Sync
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.register
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            extensions.configure<LibraryExtension> {
                configureAndroidCommon(this)
            }

            extensions.configure<KotlinAndroidProjectExtension> {
                explicitApi()
            }

            configureBinaryCompatibilityValidator()
        }
    }

    private fun Project.configureBinaryCompatibilityValidator() {
        val kotlinVersion = libs.findVersion("kotlin").get().requiredVersion
        val bcvVersion = libs.findVersion("bcv").get().requiredVersion
        val bcvRuntimeClasspath = configurations.create("maiguiBcvRuntimeClasspath") {
            isCanBeConsumed = false
            isCanBeResolved = true
        }
        dependencies.add(bcvRuntimeClasspath.name, "org.jetbrains.kotlinx:binary-compatibility-validator:$bcvVersion")
        dependencies.add(bcvRuntimeClasspath.name, "org.jetbrains.kotlin:kotlin-metadata-jvm:$kotlinVersion")
        dependencies.add(bcvRuntimeClasspath.name, "org.ow2.asm:asm:9.6")
        dependencies.add(bcvRuntimeClasspath.name, "org.ow2.asm:asm-tree:9.6")

        val generatedApiDir = layout.buildDirectory.dir("api")
        val builtApiFile = layout.buildDirectory.file("api/${project.name}.api")
        val referenceApiDir = layout.projectDirectory.dir("api")
        val referenceApiFile = referenceApiDir.file("${project.name}.api")

        val apiBuild = tasks.register<KotlinApiBuildTask>("apiBuild") {
            group = "verification"
            description = "Builds the binary API signature file for ${project.name} (release)."
            val compileJarTask = tasks.named("bundleLibCompileToJarRelease")
            dependsOn(compileJarTask)
            runtimeClasspath.from(bcvRuntimeClasspath)
            inputJar.fileProvider(compileJarTask.map { it.outputs.files.singleFile })
            outputApiFile.set(builtApiFile)
        }

        tasks.register<Sync>("apiDump") {
            group = "verification"
            description = "Dumps the public binary API of ${project.name} to api/${project.name}.api."
            dependsOn(apiBuild)
            from(generatedApiDir)
            into(referenceApiDir)
        }

        val apiCheck = tasks.register<KotlinApiCompareTask>("apiCheck") {
            group = "verification"
            description = "Verifies ${project.name} public binary API matches api/${project.name}.api."
            dependsOn(apiBuild)
            projectApiFile.set(referenceApiFile)
            this.generatedApiFile.set(builtApiFile)
        }

        tasks.matching { it.name == "check" }.configureEach {
            dependsOn(apiCheck)
        }
    }
}
