plugins {
    id("maigui.android.library")
    id("maigui.publishing")
}

android {
    namespace = "io.github.maignardi.maigui.forms"
}

maigPublishing {
    configure(
        artifactId = "maig-ui-forms",
        name = "MaigUI Forms",
        description = "Declarative form state, validators, and Brazilian input masks for MaigUI.",
    )
}

dependencies {
    api(project(":maigui-components"))

    testImplementation(libs.junit)
}
