plugins {
    `kotlin-dsl`
}

group = "io.github.maignardi.maigui.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.dokka.gradlePlugin)
    compileOnly(libs.vanniktech.publish.gradlePlugin)
    implementation(libs.bcv.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidLibrary") {
            id = "maigui.android.library"
            implementationClass = "io.github.maignardi.maigui.buildlogic.AndroidLibraryConventionPlugin"
        }
        register("androidApplication") {
            id = "maigui.android.application"
            implementationClass = "io.github.maignardi.maigui.buildlogic.AndroidApplicationConventionPlugin"
        }
        register("publishing") {
            id = "maigui.publishing"
            implementationClass = "io.github.maignardi.maigui.buildlogic.PublishingConventionPlugin"
        }
    }
}
