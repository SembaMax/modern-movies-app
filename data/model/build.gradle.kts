plugins {
    id("modernmoviesapp.android.library")
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "com.semba.modernmoviesapp.data.model"
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    implementation(libs.kotlinx.serialization)
}
