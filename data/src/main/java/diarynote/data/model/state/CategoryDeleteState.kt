package diarynote.data.model.state

sealed class CategoryDeleteState {

    object Deleting : CategoryDeleteState()
    object Success : CategoryDeleteState()
    object Idle : CategoryDeleteState()
    data class Error(val message: String) : CategoryDeleteState()
}