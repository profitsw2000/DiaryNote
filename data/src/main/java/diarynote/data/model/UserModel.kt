package diarynote.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    val id: Int,
    val login: String,
    val email: String,
    val password: String
) : Parcelable
