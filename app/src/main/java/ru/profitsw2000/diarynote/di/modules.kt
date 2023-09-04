package ru.profitsw2000.diarynote.di

import android.content.Context
import diarynote.core.utils.SHARED_PREFERENCE_NAME
import diarynote.data.room.database.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val appModule = module {
    single {
        AppDatabase.create(androidContext())
        AppDatabase.getInstance()
    }

    single {
        androidContext().getSharedPreferences(
            SHARED_PREFERENCE_NAME,
            Context.MODE_PRIVATE
        )
    }
}