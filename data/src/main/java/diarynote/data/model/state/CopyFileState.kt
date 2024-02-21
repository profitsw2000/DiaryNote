package diarynote.data.model.state

sealed class CopyFileState {
    object Copying: CopyFileState()
    data class Success(val filePath: String): CopyFileState()
    data class Error(val message: String): CopyFileState()
    object Idle: CopyFileState()
}