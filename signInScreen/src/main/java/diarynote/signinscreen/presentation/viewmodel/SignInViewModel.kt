package diarynote.signinscreen.presentation.viewmodel

import androidx.lifecycle.ViewModel
import diarynote.core.viewmodel.CoreViewModel
import java.util.regex.Pattern

class SignInViewModel : CoreViewModel() {

    fun checkInputIsValid(input: String, minLength: Int, pattern: String) : Boolean {
        val regexPattern = Pattern.compile(pattern)
        return regexPattern.matcher(input).matches() && (input.length > minLength)
    }
}