plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt") version "1.8.0"
    id("kotlin-parcelize")
}

android {
    namespace = "diarynote.data"
}

dependencies {
    //Modules
    implementation(project(Modules.core))

    //Kotlin
    implementation(Kotlin.core)
    //Design
    implementation(Design.appCompat)
    implementation(Design.material)
    //Legacy
    implementation(Legacy.legacy)
    //GSON
    implementation(GSON.gson)
    //Room
    implementation(Room.roomRuntime)
    implementation(Room.roomKtx)
    implementation(Room.roomRxJava)
    kapt(Room.roomCompiler)
    //Paging
    implementation(Paging.pagingRuntime)
    implementation(Paging.pagingKtx)
    implementation(Paging.pagingRxJava)
    //Test
    testImplementation(TestImpl.jUnit)
    androidTestImplementation(TestImpl.extJUnit)
    androidTestImplementation(TestImpl.espresso)
}