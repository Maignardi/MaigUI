plugins {
    id("maigui.android.library")
    id("maigui.publishing")
}

android {
    namespace = "io.github.maignardi.maigui.core"
}

maigPublishing {
    configure(
        artifactId = "maig-ui-core",
        name = "MaigUI Core",
        description = "Design tokens, theme system, WCAG AA palette generator, and Inter typography for MaigUI.",
    )
}

dependencies {
    api(libs.androidx.compose.runtime)
    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.ui)
    api(libs.androidx.compose.ui.graphics)
    api(libs.androidx.compose.ui.text)
    api(libs.androidx.compose.ui.unit)

    testImplementation(libs.junit)
}
