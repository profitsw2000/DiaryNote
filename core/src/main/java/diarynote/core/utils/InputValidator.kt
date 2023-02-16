package diarynote.core.utils

import java.util.regex.Pattern

class InputValidator {

    fun checkInputIsValid(input: String, pattern: String) : Boolean {
        val regexPattern = Pattern.compile(pattern)
        return regexPattern.matcher(input).matches()
    }

    fun checkInputIsValid(input: String, minLength: Int, pattern: String) : Boolean {
        val regexPattern = Pattern.compile(pattern)
        return regexPattern.matcher(input).matches() && (input.length > minLength)
    }
}