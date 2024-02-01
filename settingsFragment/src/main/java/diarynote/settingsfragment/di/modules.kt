package diarynote.settingsfragment.di

import diarynote.data.data.local.HelpRepositoryLocalImpl
import diarynote.data.data.web.HelpRepositoryRemoteImpl
import diarynote.data.domain.local.HelpRepositoryLocal
import diarynote.data.domain.web.HelpRepositoryRemote
import diarynote.data.interactor.SettingsInteractor
import diarynote.settingsfragment.presentation.viewmodel.SettingsViewModel
import org.koin.dsl.module

val settingsModule = module{

    single { SettingsViewModel(get(), get(), get(), get()) }
    single<HelpRepositoryLocal> { HelpRepositoryLocalImpl() }
    single<HelpRepositoryRemote> { HelpRepositoryRemoteImpl() }
    factory { SettingsInteractor(get(), get(), get(), get()) }
}