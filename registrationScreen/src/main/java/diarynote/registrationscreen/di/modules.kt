package diarynote.registrationscreen.di

import diarynote.registrationscreen.presentation.viewmodel.RegistrationViewModel
import org.koin.dsl.module

val registrationModule = module{
    single { RegistrationViewModel(get(), get(), get(), get()) }
}