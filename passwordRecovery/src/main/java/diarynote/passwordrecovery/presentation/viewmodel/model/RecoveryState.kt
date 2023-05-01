package diarynote.passwordrecovery.presentation.viewmodel.model

sealed class RecoveryState {
    object Success: RecoveryState()
    data class Error(val code: Int): RecoveryState()
    object Loading: RecoveryState()
}
