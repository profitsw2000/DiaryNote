package diarynote.readnotescreen.di

import diarynote.readnotescreen.presentation.viewmodel.ReadNoteViewModel
import org.koin.dsl.module

val readNoteModule = module {
    single { ReadNoteViewModel(get(), get()) }
}