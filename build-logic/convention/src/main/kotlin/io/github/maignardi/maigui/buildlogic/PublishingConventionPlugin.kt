package io.github.maignardi.maigui.buildlogic

import com.vanniktech.maven.publish.AndroidSingleVariantLibrary
import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.MavenPublishBaseExtension
import com.vanniktech.maven.publish.SourcesJar
import javax.inject.Inject
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

open class MaigPublishingExtension @Inject constructor(
    private val project: Project,
) {
    fun configure(
        artifactId: String,
        name: String,
        description: String,
    ) {
        val groupId = project.providers.gradleProperty("GROUP")
            .orElse(project.providers.gradleProperty("group"))
            .getOrElse("io.github.maignardi")
        val versionName = project.providers.gradleProperty("VERSION_NAME")
            .orElse(project.providers.gradleProperty("version"))
            .getOrElse("0.1.0")

        project.extensions.configure<MavenPublishBaseExtension> {
            coordinates(groupId, artifactId, versionName)
            pom {
                this.name.set(name)
                this.description.set(description)
            }
        }
    }
}

class PublishingConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.vanniktech.maven.publish")
                apply("org.jetbrains.dokka")
            }

            extensions.create("maigPublishing", MaigPublishingExtension::class.java, target)

            extensions.configure<MavenPublishBaseExtension> {
                configure(
                    AndroidSingleVariantLibrary(
                        variant = "release",
                        sourcesJar = SourcesJar.Sources(),
                        javadocJar = JavadocJar.Dokka("dokkaGeneratePublicationHtml"),
                    ),
                )

                publishToMavenCentral(automaticRelease = true)

                val hasSigningKey =
                    providers.gradleProperty("signingInMemoryKey").isPresent ||
                        providers.gradleProperty("signing.keyId").isPresent ||
                        providers.environmentVariable("ORG_GRADLE_PROJECT_signingInMemoryKey").isPresent
                val isMavenCentralPublish = gradle.startParameter.taskNames.any {
                    it.contains("MavenCentral", ignoreCase = true)
                }
                if (hasSigningKey || isMavenCentralPublish) {
                    signAllPublications()
                }
            }
        }
    }
}
