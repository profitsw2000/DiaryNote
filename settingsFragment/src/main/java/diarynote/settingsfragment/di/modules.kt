package diarynote.settingsfragment.di

import diarynote.data.interactor.SettingsInteractor
import diarynote.settingsfragment.presentation.viewmodel.SettingsViewModel
import org.koin.dsl.module

val settingsModule = module{

    single { SettingsViewModel(get()) }
    factory { SettingsInteractor() }
}