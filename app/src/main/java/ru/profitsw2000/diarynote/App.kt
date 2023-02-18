package ru.profitsw2000.diarynote

import android.app.Application
import diarynote.registrationscreen.di.registrationModule
import diarynote.signinscreen.di.signInModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import ru.profitsw2000.diarynote.di.appModule

//модуль App
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(appModule, signInModule, registrationModule)
        }
    }
}