package diarynote.createnotescreen.di

import diarynote.createnotescreen.presentation.viewmodel.CreateNoteViewModel
import org.koin.dsl.module

val createNoteModule = module {
    single { CreateNoteViewModel(get(), get(), get(), get(), get(), get()) }
}