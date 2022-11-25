package com.example.hhgame.utils.state

import androidx.annotation.StringRes

sealed class TextInputState {
    object Default : TextInputState()
    object Acceptable : TextInputState()
    data class Error(@StringRes val messageResId: Int) : TextInputState()
}