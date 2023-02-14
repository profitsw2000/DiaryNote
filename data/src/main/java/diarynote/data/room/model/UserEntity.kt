package diarynote.data.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val login: String,
    val email: String,
    val password: String
)