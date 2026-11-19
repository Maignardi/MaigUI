plugins {
    id("maigui.android.library")
    id("maigui.publishing")
    alias(libs.plugins.roborazzi)
}

android {
    namespace = "io.github.maignardi.maigui.components"
}

maigPublishing {
    configure(
        artifactId = "maig-ui",
        name = "MaigUI",
        description = "Bootstrap-style Jetpack Compose UI component library built on foundation and MaigTheme.",
    )
}

dependencies {
    api(project(":maigui-core"))
    implementation(libs.androidx.compose.material.ripple)

    debugImplementation(libs.androidx.compose.ui.test.manifest)

    testImplementation(libs.junit)
    testImplementation(libs.robolectric)
    testImplementation(libs.asm)
    testImplementation(libs.asm.commons)
    testImplementation(libs.asm.tree)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.androidx.compose.ui.test.junit4)
    testImplementation(libs.roborazzi)
    testImplementation(libs.roborazzi.compose)
    testImplementation(libs.roborazzi.junit.rule)
}
