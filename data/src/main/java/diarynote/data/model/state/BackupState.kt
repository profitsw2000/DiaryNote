package diarynote.data.model.state

import android.net.Uri

sealed class BackupState{
    object SuccessBackup : BackupState()
    object SuccessRestore : BackupState()
    data class DbState(val isEncrypted: Boolean, val uri: Uri) : BackupState()
    data class Error(val message: String, val errorCode: Int) : BackupState()
    object Loading : BackupState()
    object Idle : BackupState()
}
