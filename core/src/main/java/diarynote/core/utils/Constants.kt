package diarynote.core.utils


val SHARED_PREFERENCE_NAME = "SHARED_PREFERENCE_NAME"

const val EMAIL_PATTERN = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
        "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." +
        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + ")+"
const val LOGIN_PATTERN = "[A-Za-z0-9]+"
const val LOGIN_MIN_LENGTH = 4
const val PASSWORD_PATTERN = "[A-Za-z0-9]+"
const val PASSWORD_MIN_LENGTH = 8
const val LOGIN_BIT_NUMBER = 0
const val EMAIL_BIT_NUMBER = 1
const val INVALID_PASSWORD_BIT_NUMBER = 1
const val PASSWORD_BIT_NUMBER = 2
const val CONFIRM_PASSWORD_BIT_NUMBER = 3
const val NOTE_TITLE_BIT_NUMBER = 0
const val NOTE_CONTENT_BIT_NUMBER = 1
const val NOTE_TAGS_BIT_NUMBER = 2
const val NOTE_TAG_WORDS_BIT_NUMBER = 3
const val ROOM_BIT_NUMBER = 4
const val LOGIN_ALREADY_EXIST_BIT_NUMBER = 5
const val EMAIL_ALREADY_EXIST_BIT_NUMBER = 6
const val NOTE_TITLE_MIN_LENGTH = 2
const val NOTE_CONTENT_MIN_LENGTH = 10
const val NOTE_TAGS_MIN_LENGTH = 2
const val NOTE_TAG_WORDS_LIMIT = 2