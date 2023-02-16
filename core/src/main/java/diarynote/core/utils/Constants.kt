package diarynote.core.utils

const val EMAIL_PATTERN = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
        "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." +
        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + ")+"
const val LOGIN_PATTERN = "[A-Za-z0-9]+"
const val LOGIN_MIN_LENGTH = 3
const val PASSWORD_PATTERN = "[A-Za-z0-9]+"
const val PASSWORD_MIN_LENGTH = 7