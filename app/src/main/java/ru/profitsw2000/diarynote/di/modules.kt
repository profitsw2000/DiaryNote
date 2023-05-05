package ru.profitsw2000.diarynote.di

import android.content.Context
import diarynote.data.room.database.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val SHARED_PREFERENCE_NAME = "SHARED_PREFERENCE_NAME"

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