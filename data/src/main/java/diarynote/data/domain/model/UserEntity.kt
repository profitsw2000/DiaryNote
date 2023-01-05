package diarynote.data.domain.model

data class UserEntity(
    val id: Int = 0,
    val login: String = "",
    val email: String = "",
    val password: String = ""
)
