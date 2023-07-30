import org.gradle.api.JavaVersion

object Config{
    const val application_id = "ru.profitsw2000.diarynote"
    const val compile_sdk = 33
    const val min_sdk = 23
    const val target_sdk = 33
    val java_version = JavaVersion.VERSION_17
}

object Releases {
    const val version_code = 1
    const val version_name = "1.0"
}

object Modules {
    const val app = ":app"
    const val core = ":core"
    const val data = ":data"
    const val signinscreen = ":signInScreen"
    const val registrationscreen = ":registrationScreen"
    const val passwordrecovery = ":passwordRecovery"
    const val notesactivity = ":notesActivity"
    const val navigator = ":navigator"
    const val mainfragment = ":mainFragment"
    const val categoriesfragment = ":categoriesFragment"
    const val calendarfragment = ":calendarFragment"
    const val settingsfragment = ":settingsFragment"
    const val createnotescreen = ":createNoteScreen"
    const val readnotescreen = ":readNoteScreen"
    const val editnotescreen = ":editNoteScreen"
    const val addcategoryscreen = ":addCategoryScreen"
}

object Versions {
    //Design
    const val appcompat = "1.5.1"
    const val material = "1.6.1"
    const val constraintLayout = "2.1.4"
    const val fragment ="1.3.0"

    //Kotlin
    const val core = "1.9.0"

    //Lifecycle
    const val viewModel = "2.5.1"

    //Navigation
    const val navigation = "2.6.0-alpha04"

    //Legacy
    const val legacy = "1.0.0"

    //RxJava
    const val rxJava = "3.1.5"
    const val rxKotlin = "3.0.1"
    const val rxAndroid = "3.0.0"

    //Koin
    const val koin = "3.2.0"

    //GSON
    const val gson = "2.9.0"

    //Room
    const val room = "2.4.3"

    //Test
    const val jUnit = "4.13.2"
    const val extJUnit = "1.1.4"
    const val espressoCore = "3.5.0"
}

object Design {
    const val appCompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    const val material = "com.google.android.material:material:${Versions.material}"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val fragment = "androidx.fragment:fragment:${Versions.fragment}"
}

object Kotlin {
    const val core = "androidx.core:core-ktx:${Versions.core}"
}

object ViewModel {
    const val liveData = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.viewModel}"
    const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.viewModel}"
}

object Navigation {
    const val navigationRuntime = "androidx.navigation:navigation-runtime-ktx:${Versions.navigation}"
    const val navigationFragment = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val navigationUI = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
}

object Legacy {
    const val legacy = "androidx.legacy:legacy-support-v4:${Versions.legacy}"
}

object RxJava {
    const val rxJava = "io.reactivex.rxjava3:rxjava:${Versions.rxJava}"
    const val rxKotlin = "io.reactivex.rxjava3:rxkotlin:${Versions.rxKotlin}"
    const val rxAndroid = "io.reactivex.rxjava3:rxandroid:${Versions.rxAndroid}"
}

object Koin {
    const val koin = "io.insert-koin:koin-android:${Versions.koin}"
}

object GSON {
    const val gson = "com.google.code.gson:gson:${Versions.gson}"
}

object Room {
    const val roomRuntime = "androidx.room:room-runtime:${Versions.room}"
    const val roomKtx = "androidx.room:room-ktx:${Versions.room}"
    const val roomRxJava = "androidx.room:room-rxjava3:${Versions.room}"
    const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"
}

object TestImpl {
    const val jUnit = "junit:junit:${Versions.jUnit}"
    const val extJUnit = "androidx.test.ext:junit:${Versions.extJUnit}"
    const val espresso = "androidx.test.espresso:espresso-core:${Versions.espressoCore}"
}
