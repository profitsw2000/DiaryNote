package diarynote.data.model

data class UserModel(
    val id: Int? = 0,
    val login: String? = "",
    val email: String? = "",
    val password: String? = ""
)
