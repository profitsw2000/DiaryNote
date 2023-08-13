package diarynote.categoriesfragment.di

import diarynote.categoriesfragment.presentation.viewmodel.CategoriesViewModel
import diarynote.data.data.local.CategoryRepositoryLocalImpl
import diarynote.data.data.local.NoteRepositoryLocalImpl
import diarynote.data.data.web.CategoryRepositoryRemoteImpl
import diarynote.data.data.web.NoteRepositoryRemoteImpl
import diarynote.data.domain.local.CategoryRepositoryLocal
import diarynote.data.domain.local.NoteRepositoryLocal
import diarynote.data.domain.web.CategoryRepositoryRemote
import diarynote.data.domain.web.NoteRepositoryRemote
import diarynote.data.interactor.CategoryInteractor
import diarynote.data.interactor.NoteInteractor
import diarynote.data.mappers.CategoryMapper
import diarynote.data.mappers.NoteMapper
import org.koin.dsl.module


val categoryModule = module {
    single { CategoriesViewModel(get(), get(), get(), get(), get()) }
    single<CategoryRepositoryLocal> { CategoryRepositoryLocalImpl(get()) }
    single<CategoryRepositoryRemote> { CategoryRepositoryRemoteImpl() }
    single<NoteRepositoryLocal> { NoteRepositoryLocalImpl(get()) }
    single<NoteRepositoryRemote> { NoteRepositoryRemoteImpl() }
    factory { CategoryInteractor(get(), get()) }
    factory { NoteInteractor(get(), get()) }
    factory { CategoryMapper() }
    factory { NoteMapper() }
}