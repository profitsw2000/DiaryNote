plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt") version "1.8.0"
}

android {
    namespace = "diarynote.createnotescreen"
}

dependencies {

    //Modules
    implementation(project(Modules.core))
    implementation(project(Modules.data))
    implementation(project(Modules.navigator))
    implementation(project(Modules.template))

    //Kotlin
    implementation(Kotlin.core)
    //Design
    implementation(Design.appCompat)
    implementation(Design.material)
    implementation(Design.constraintLayout)
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
    kapt(Room.roomCompiler)
    //Coil
    implementation(Coil.coil)
    implementation(Coil.coilSvg)
    //Test
    testImplementation(TestImpl.jUnit)
    androidTestImplementation(TestImpl.extJUnit)
    androidTestImplementation(TestImpl.espresso)
}