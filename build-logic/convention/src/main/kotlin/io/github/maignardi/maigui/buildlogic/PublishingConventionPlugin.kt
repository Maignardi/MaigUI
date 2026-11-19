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
        val groupId = project.providers.gradleProperty("group")
            .orElse(project.providers.gradleProperty("GROUP"))
            .getOrElse("io.github.maignardi")
        val versionName = project.providers.gradleProperty("version")
            .orElse(project.providers.gradleProperty("VERSION_NAME"))
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
                publishToMavenCentral()

                if (
                    providers.gradleProperty("signingInMemoryKey").isPresent ||
                    providers.gradleProperty("signing.keyId").isPresent
                ) {
                    signAllPublications()
                }

                pom {
                    url.set(providers.gradleProperty("POM_URL"))
                    licenses {
                        license {
                            name.set(providers.gradleProperty("POM_LICENCE_NAME"))
                            url.set(providers.gradleProperty("POM_LICENCE_URL"))
                            distribution.set(providers.gradleProperty("POM_LICENCE_DIST"))
                        }
                    }
                    developers {
                        developer {
                            id.set(providers.gradleProperty("POM_DEVELOPER_ID"))
                            name.set(providers.gradleProperty("POM_DEVELOPER_NAME"))
                            url.set(providers.gradleProperty("POM_DEVELOPER_URL"))
                        }
                    }
                    scm {
                        url.set(providers.gradleProperty("POM_SCM_URL"))
                        connection.set(providers.gradleProperty("POM_SCM_CONNECTION"))
                        developerConnection.set(providers.gradleProperty("POM_SCM_DEV_CONNECTION"))
                    }
                }
            }
        }
    }
}
