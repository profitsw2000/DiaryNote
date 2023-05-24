plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt") version "1.8.0"
}

android {
    namespace = "diarynote.notesactivity"
}

dependencies {
    //Modules
    implementation(project(Modules.core))
    implementation(project(Modules.data))
    implementation(project(Modules.navigator))
    implementation(project(Modules.mainfragment))
    implementation(project(Modules.categoriesfragment))
    implementation(project(Modules.calendarfragment))
    implementation(project(Modules.settingsfragment))
    implementation(project(Modules.createnotescreen))
    implementation(project(Modules.readnotescreen))

    //Kotlin
    implementation(Kotlin.core)
    //Design
    implementation(Design.appCompat)
    implementation(Design.material)
    implementation(Design.constraintLayout)
    //Navigation
    implementation(Navigation.navigationRuntime)
    implementation(Navigation.navigationFragment)
    implementation(Navigation.navigationUI)
    //ViewModel
    implementation(ViewModel.liveData)
    implementation(ViewModel.viewModel)
    //Legacy
    implementation(Legacy.legacy)
    //Koin
    implementation(Koin.koin)
    //Test
    testImplementation(TestImpl.jUnit)
    androidTestImplementation(TestImpl.extJUnit)
    androidTestImplementation(TestImpl.espresso)
}