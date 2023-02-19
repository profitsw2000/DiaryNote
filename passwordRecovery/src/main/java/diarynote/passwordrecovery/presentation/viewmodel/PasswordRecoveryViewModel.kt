package diarynote.passwordrecovery.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import diarynote.core.utils.EMAIL_BIT_NUMBER
import diarynote.core.utils.EMAIL_PATTERN
import diarynote.core.utils.InputValidator
import diarynote.core.utils.ROOM_BIT_NUMBER
import diarynote.core.viewmodel.CoreViewModel
import diarynote.data.interactor.UserInteractor
import diarynote.passwordrecovery.model.RecoveryState
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class PasswordRecoveryViewModel(
    private val userInteractor: UserInteractor
) : CoreViewModel() {

    private val inputValidator = InputValidator()

    private val _recoveryLiveData = MutableLiveData<RecoveryState>()
    val recoveryState: LiveData<RecoveryState> by this::_recoveryLiveData

    fun passwordRecovery(email: String) {
        val emailIsValid = inputValidator.checkInputIsValid(email, EMAIL_PATTERN)

        if (emailIsValid) {
            sendEmail(email)
        } else {
            _recoveryLiveData.value = RecoveryState.Error(1 shl EMAIL_BIT_NUMBER)
        }
    }

    private fun sendEmail(email: String) {
        _recoveryLiveData.value = RecoveryState.Loading

        userInteractor.getUserByEmail(email, false)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _recoveryLiveData.value = RecoveryState.Success
                },
                {
                    _recoveryLiveData.value = RecoveryState.Error(1 shl ROOM_BIT_NUMBER)
                }
            )
    }
}