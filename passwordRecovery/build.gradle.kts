plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt") version "1.8.0"
}

android {
    namespace = "diarynote.passwordrecovery"
}

dependencies {
    //Modules
    implementation(project(Modules.core))
    implementation(project(Modules.data))

    //Kotlin
    implementation(Kotlin.core)
    //Design
    implementation(Design.appCompat)
    implementation(Design.material)
    implementation(Design.constraintLayout)
    implementation(Design.material)
    //ViewModel
    implementation(ViewModel.liveData)
    implementation(ViewModel.viewModel)
    //Legacy
    implementation(Legacy.legacy)
    //RxJava
    implementation(RxJava.rxJava)
    implementation(RxJava.rxAndroid)
    implementation(RxJava.rxKotlin)
    //Koin
    implementation(Koin.koin)
    //Room
    implementation(Room.roomRuntime)
    implementation(Room.roomKtx)
    implementation(Room.roomRxJava)
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.5.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
    kapt(Room.roomCompiler)
    //Test
    testImplementation(TestImpl.jUnit)
    androidTestImplementation(TestImpl.extJUnit)
    androidTestImplementation(TestImpl.espresso)
}