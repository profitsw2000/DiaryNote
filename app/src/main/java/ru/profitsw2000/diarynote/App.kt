package ru.profitsw2000.diarynote

import android.app.Application
import diarynote.categoriesfragment.di.categoryModule
import diarynote.createnotescreen.di.createNoteModule
import diarynote.mainfragment.di.homeModule
import diarynote.passwordrecovery.di.recoveryModule
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
            modules(appModule,
                signInModule,
                registrationModule,
                recoveryModule,
                categoryModule,
                homeModule,
                createNoteModule
            )
        }
    }
}