package diarynote.data.room.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["login"], unique = true), Index(value = ["email"], unique = true)])
data class UserEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val login: String,
    val email: String,
    val password: String
)