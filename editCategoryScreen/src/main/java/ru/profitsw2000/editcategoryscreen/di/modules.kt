package ru.profitsw2000.editcategoryscreen.di

import org.koin.dsl.module
import ru.profitsw2000.editcategoryscreen.presentation.viewmodel.EditCategoryViewModel

val editCategoryModule = module {
    single { EditCategoryViewModel(get(), get()) }
}
