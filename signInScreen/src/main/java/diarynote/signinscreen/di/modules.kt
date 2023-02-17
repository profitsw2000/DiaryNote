package diarynote.signinscreen.di

import diarynote.data.data.local.UserRepositoryLocalImpl
import diarynote.data.data.web.UserRepositoryRemoteImpl
import diarynote.data.domain.local.UserRepositoryLocal
import diarynote.data.domain.web.UserRepositoryRemote
import diarynote.data.interactor.UserInteractor
import diarynote.data.mappers.UserMapper
import diarynote.signinscreen.presentation.viewmodel.SignInViewModel
import org.koin.dsl.module

val signInModule = module {
    single { SignInViewModel(get()) }
    single<UserRepositoryLocal> { UserRepositoryLocalImpl(get()) }
    single<UserRepositoryRemote> { UserRepositoryRemoteImpl() }
    factory { UserInteractor(get(), get()) }
    factory { UserMapper() }
}