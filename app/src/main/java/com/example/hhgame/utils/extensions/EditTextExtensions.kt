package com.example.hhgame.utils.extensions

import com.example.hhgame.utils.state.TextInputState
import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.handleInputText(state: TextInputState) {
    when (state) {
        is TextInputState.Default -> {
            this.error = null
        }
        is TextInputState.Error -> {
            this.error = resources.getString(state.messageResId)
        }
        is TextInputState.Acceptable -> {
            this.error = null
        }
    }
}