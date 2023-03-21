plugins {
    id("modernmoviesapp.android.library")
    id("modernmoviesapp.android.hilt")
}

android {
    namespace = "com.semba.modernmoviesapp.core.common"
}

dependencies {
    implementation(libs.kotlinx.coroutines.android)
}