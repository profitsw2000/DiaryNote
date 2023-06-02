package diarynote.addcategoryscreen.di

import diarynote.addcategoryscreen.presentation.viewmodel.AddCategoryViewModel
import org.koin.dsl.module

val addCategoryModule = module {
    single { AddCategoryViewModel(get(), get(), get(), get()) }
}