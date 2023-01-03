plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "ru.profitsw2000.signinscreen"
}

dependencies {
    //Modules
    implementation(project(":core"))
    implementation(project(":data"))

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
    implementation(Room.roomCompiler)
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.5.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
    //Test
    testImplementation(TestImpl.jUnit)
    androidTestImplementation(TestImpl.extJUnit)
    androidTestImplementation(TestImpl.espresso)
}