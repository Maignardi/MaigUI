plugins {
    id("maigui.android.library")
    id("maigui.publishing")
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

    testImplementation(libs.junit)
}
