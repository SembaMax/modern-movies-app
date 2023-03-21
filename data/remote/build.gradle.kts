import java.util.Properties

plugins {
    id("modernmoviesapp.android.library")
    id("modernmoviesapp.android.hilt")
    alias(libs.plugins.kotlinx.serialization)
}

android {
    val properties = Properties()
    properties.load(project.rootProject.file("local.properties").inputStream())
    defaultConfig {
        buildConfigField("String", "API_KEY", "\"${properties.getProperty("API_KEY")}\"")
    }

    buildFeatures {
        buildConfig = true
    }
    namespace = "com.semba.modernmoviesapp.data.remote"
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":data:model"))

    implementation(libs.kotlinx.serialization)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.okhttp3.logging.interceptor)
    implementation(libs.retrofit2)
    implementation(libs.retrofit2.kotlin.serialization)
}
