package diarynote.signinscreen.di

import diarynote.signinscreen.presentation.viewmodel.SignInViewModel
import org.koin.dsl.module

val signInModule = module {
    single { SignInViewModel() }
}