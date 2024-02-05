package diarynote.data.model.state

sealed class CopyFileState {
    object Copying: CopyFileState()
    class Success(filePath: String): CopyFileState()
    class Error(message: String): CopyFileState()
    object Idle: CopyFileState()
}