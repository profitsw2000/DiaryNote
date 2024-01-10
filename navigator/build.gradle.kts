plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt") version "1.8.0"
}

android {
    namespace = "diarynote.navigator"
}

dependencies {
    //Kotlin
    implementation(Kotlin.core)
    //Design
    implementation(Design.appCompat)
    implementation(Design.material)
    //Test
    testImplementation(TestImpl.jUnit)
    androidTestImplementation(TestImpl.extJUnit)
    androidTestImplementation(TestImpl.espresso)
}