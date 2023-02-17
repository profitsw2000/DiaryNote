package diarynote.core.utils

const val EMAIL_PATTERN = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
        "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." +
        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + ")+"
const val LOGIN_PATTERN = "[A-Za-z0-9]+"
const val LOGIN_MIN_LENGTH = 3
const val PASSWORD_PATTERN = "[A-Za-z0-9]+"
const val PASSWORD_MIN_LENGTH = 7
const val LOGIN_BIT_NUMBER = 0
const val EMAIL_BIT_NUMBER = 1
const val INVALID_PASSWORD_BIT_NUMBER = 1
const val PASSWORD_BIT_NUMBER = 2
const val CONFIRM_PASSWORD_BIT_NUMBER = 3
const val ROOM_BIT_NUMBER = 4
const val LOGIN_ALREADY_EXIST_BIT_NUMBER = 5
const val EMAIL_ALREADY_EXIST_BIT_NUMBER = 6