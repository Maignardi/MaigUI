plugins {
    id("maigui.android.application")
}

android {
    namespace = "io.github.maignardi.maigui.sample"

    defaultConfig {
        applicationId = "io.github.maignardi.maigui.sample"
        versionCode = 1
        versionName = "0.1.0"
    }
}

dependencies {
    implementation(project(":maigui-core"))
    implementation(project(":maigui-components"))
    implementation(project(":maigui-forms"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)

    debugImplementation(libs.androidx.compose.ui.test.manifest)

    testImplementation(libs.junit)
    testImplementation(libs.robolectric)
    testImplementation(libs.asm)
    testImplementation(libs.asm.commons)
    testImplementation(libs.asm.tree)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.androidx.compose.ui.test.junit4)
}
