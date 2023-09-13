package diarynote.editnotescreen.di

import diarynote.editnotescreen.presentation.viewmodel.EditNoteViewModel
import org.koin.dsl.module

val editNoteModule = module{
    single { EditNoteViewModel(get(), get(), get(), get(), get()) }
}