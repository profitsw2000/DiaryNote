package diarynote.categoriesfragment.presentation.view.di

import diarynote.categoriesfragment.presentation.viewmodel.CategoriesViewModel
import diarynote.data.data.local.CategoryRepositoryLocalImpl
import diarynote.data.data.web.CategoryRepositoryRemoteImpl
import diarynote.data.domain.local.CategoryRepositoryLocal
import diarynote.data.domain.web.CategoryRepositoryRemote
import diarynote.data.interactor.CategoryInteractor
import diarynote.data.mappers.CategoryMapper
import org.koin.dsl.module


val categoryModule = module {
    single { CategoriesViewModel(get(), get(), get()) }
    single<CategoryRepositoryLocal> { CategoryRepositoryLocalImpl(get()) }
    single<CategoryRepositoryRemote> { CategoryRepositoryRemoteImpl() }
    factory { CategoryInteractor(get(), get()) }
    factory { CategoryMapper() }
}