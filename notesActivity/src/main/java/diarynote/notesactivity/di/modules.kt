package diarynote.notesactivity.di

import diarynote.notesactivity.presentation.viewmodel.NoteViewModel
import org.koin.dsl.module

val noteModule = module {
    single { NoteViewModel(get()) }
}
