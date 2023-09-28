package diarynote.passwordrecovery.di

import diarynote.passwordrecovery.presentation.viewmodel.PasswordRecoveryViewModel
import org.koin.dsl.module

val recoveryModule = module {
    single { PasswordRecoveryViewModel(get()) }
}