package diarynote.mainfragment.di

import diarynote.data.data.local.NoteRepositoryLocalImpl
import diarynote.data.data.web.NoteRepositoryRemoteImpl
import diarynote.data.domain.local.NoteRepositoryLocal
import diarynote.data.domain.web.NoteRepositoryRemote
import diarynote.data.interactor.NoteInteractor
import diarynote.data.mappers.NoteMapper
import diarynote.mainfragment.presentation.viewmodel.HomeViewModel
import org.koin.dsl.module

val homeModule = module {

    single { HomeViewModel(get(), get(), get(), get()) }
    single<NoteRepositoryLocal> { NoteRepositoryLocalImpl(get()) }
    single<NoteRepositoryRemote> { NoteRepositoryRemoteImpl() }
    factory { NoteInteractor(get(), get()) }
    factory { NoteMapper() }
}