package com.example.SlothGaming.utils

import android.net.Uri
import android.util.Patterns

object InputValidator {

    private const val MIN_PASSWORD_LENGTH = 8
    private const val MIN_TITLE_LENGTH = 3

    fun validateLogin(email: String, password: String, emptyFieldsError: String): String? {
        return if (email.isEmpty() || password.isEmpty()) emptyFieldsError else null
    }

    fun validateSignUp(
        firstName: String, lastName: String, email: String,
        phoneNumber: String, password: String,
        emptyFieldsError: String, emailFormatError: String, passwordShortError: String
    ): String? {
        return when {
            firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()
                    || phoneNumber.isEmpty() || password.isEmpty() -> emptyFieldsError
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> emailFormatError
            password.length < MIN_PASSWORD_LENGTH -> passwordShortError
            else -> null
        }
    }

    fun validateReview(
        consoleType: String, title: String, description: String, imageUri: Uri?,
        platformError: String, titleError: String, descriptionError: String, imageError: String
    ): Pair<ReviewField, String>? {
        return when {
            consoleType.isEmpty() -> ReviewField.CONSOLE to platformError
            title.length < MIN_TITLE_LENGTH -> ReviewField.TITLE to titleError
            description.isEmpty() -> ReviewField.DESCRIPTION to descriptionError
            imageUri == null -> ReviewField.IMAGE to imageError
            else -> null
        }
    }

    enum class ReviewField {
        CONSOLE, TITLE, DESCRIPTION, IMAGE
    }
}
