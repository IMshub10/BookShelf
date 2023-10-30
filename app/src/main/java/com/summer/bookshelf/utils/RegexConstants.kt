package com.summer.bookshelf.utils

object RegexConstants {
    const val emailRegex = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}\$"

    const val passwordRegex = "^(?=.*[0-9])(?=.*[!@#\$%&()])(?=.*[a-z])(?=.*[A-Z]).{8,}$"

    const val containsDigitRegex = ".*\\d+.*"

    const val containsLowerAlphabets = ".*[a-z]+.*"

    const val containsUpperAlphabets = ".*[A-Z]+.*"

    const val containsSpecialCharacter = ".*[!@#\$%&()*]+.*"
}