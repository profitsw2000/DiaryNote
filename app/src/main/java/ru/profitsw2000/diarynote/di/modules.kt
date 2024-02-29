package ru.profitsw2000.diarynote.di

import android.content.Context
import coil.ImageLoader
import coil.decode.SvgDecoder
import diarynote.core.utils.SHARED_PREFERENCE_NAME
import diarynote.data.room.database.AppDatabase
import diarynote.data.room.utils.PassphraseGenerator
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.profitsw2000.diarynote.presentation.viewmodel.MainViewModel


val appModule = module {

    single {
        val passphraseGenerator: PassphraseGenerator = get()

        AppDatabase.create(androidContext(), passphraseGenerator.getPassphrase())
        AppDatabase.getInstance()
    }

    single {
        androidContext().getSharedPreferences(
            SHARED_PREFERENCE_NAME,
            Context.MODE_PRIVATE
        )
    }

    single {
        ImageLoader.Builder(androidContext())
        .components {
            add(SvgDecoder.Factory())
        }
        .build()
    }

    single { MainViewModel(get()) }
}