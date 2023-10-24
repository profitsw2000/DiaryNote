package diarynote.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    val id: Int,
    val name: String,
    val surname: String,
    val imagePath: String,
    val login: String,
    val email: String,
    val password: String
) : Parcelable
