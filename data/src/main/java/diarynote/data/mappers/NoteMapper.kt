package diarynote.data.mappers

import diarynote.data.model.CategoryModel
import diarynote.data.model.NoteModel
import diarynote.data.room.entity.CategoryEntity
import diarynote.data.room.entity.NoteEntity

class NoteMapper {

    fun map(noteModel: NoteModel): NoteEntity {
        return NoteEntity(
            id = noteModel.id,
            category = noteModel.category,
            title = noteModel.title,
            text = noteModel.text,
            tags = noteModel.tags,
            image = noteModel.image,
            date = noteModel.date,
            edited = noteModel.edited,
            editDate = noteModel.editDate,
            userId = noteModel.userId
        )
    }

    fun map(noteEntity: NoteEntity): NoteModel {
        return NoteModel(
            id = noteEntity.id,
            category = noteEntity.category,
            title = noteEntity.title,
            text = noteEntity.text,
            tags = noteEntity.tags,
            image = noteEntity.image,
            date = noteEntity.date,
            edited = noteEntity.edited,
            editDate = noteEntity.editDate,
            userId = noteEntity.userId
        )
    }

    @Suppress("INAPPLICABLE_JVM_NAME")
    @JvmName("mapEntityList")
    fun map(noteEntityList: List<NoteEntity>): List<NoteModel> {
        return noteEntityList.map { map(it) }
    }

    @Suppress("INAPPLICABLE_JVM_NAME")
    @JvmName("mapModelList")
    fun map(noteModelList: List<NoteModel>): List<NoteEntity> {
        return noteModelList.map { map(it) }
    }
}