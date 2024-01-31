package diarynote.template.model

sealed class BackupState{
    object SuccessBackup : BackupState()
    object SuccessRestore : BackupState()
    data class Error(val message: String, val errorCode: Int) : BackupState()
    object Loading : BackupState()
    object Idle : BackupState()
}
