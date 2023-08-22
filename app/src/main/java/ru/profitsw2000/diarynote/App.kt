package ru.profitsw2000.diarynote

import android.app.Application
import diarynote.addcategoryscreen.di.addCategoryModule
import diarynote.calendarfragment.di.calendarModule
import diarynote.categoriesfragment.di.categoryModule
import diarynote.createnotescreen.di.createNoteModule
import diarynote.editnotescreen.di.editNoteModule
import diarynote.mainfragment.di.homeModule
import diarynote.passwordrecovery.di.recoveryModule
import diarynote.readnotescreen.di.readNoteModule
import diarynote.registrationscreen.di.registrationModule
import diarynote.settingsfragment.di.settingsModule
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
                createNoteModule,
                readNoteModule,
                editNoteModule,
                addCategoryModule,
                calendarModule,
                settingsModule
            )
        }
    }
}