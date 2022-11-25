package com.example.hhgame.presentation.parameters_fragments.viewmodel

import androidx.lifecycle.ViewModel
import com.example.hhgame.R
import com.example.hhgame.utils.state.ParametersScreenState
import com.example.hhgame.utils.state.TextInputState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class ParametersViewModel : ViewModel() {

    private val attack = MutableStateFlow<TextInputState>(TextInputState.Default)
    private val health = MutableStateFlow<TextInputState>(TextInputState.Default)
    private val protection = MutableStateFlow<TextInputState>(TextInputState.Default)
    private val minDamage = MutableStateFlow<TextInputState>(TextInputState.Default)
    private val maxDamage = MutableStateFlow<TextInputState>(TextInputState.Default)

    val state = combine(
        attack,
        health,
        protection,
        minDamage,
        maxDamage,
    ) { stateArray ->
        ParametersScreenState(
            attack = stateArray[ATTACK_TEXT_INPUT],
            health = stateArray[HEALTH_TEXT_INPUT],
            protection = stateArray[PROTECTION_TEXT_INPUT],
            minDamage = stateArray[MIN_DAMAGE_TEXT_INPUT],
            maxDamage = stateArray[MAX_DAMAGE_TEXT_INPUT]
        )
    }

    fun healthChanged(value: String) {
        if (value.isEmpty()) health.value = TextInputState.Default else
            health.value = checkErrors(
                value,
                MIN_HP_VALUE,
                MAX_HP_VALUE
            )
    }

    fun protectionChanged(value: String) {
        if (value.isEmpty()) protection.value = TextInputState.Default else
            protection.value = checkErrors(
                value,
                ONE,
                TWENTY
            )
    }

    fun attackChanged(value: String) {
        if (value.isEmpty()) attack.value = TextInputState.Default else
            attack.value = checkErrors(
                value,
                ONE,
                TWENTY
            )
    }

    fun minDamageChanged(value: String) {
        if (value.isEmpty()) minDamage.value = TextInputState.Default else
            minDamage.value = checkErrors(
                value,
                ONE,
                THREE
            )
    }

    fun maxDamageChanged(value: String) {
        if (value.isEmpty()) maxDamage.value = TextInputState.Default else
            maxDamage.value = checkErrors(
                value,
                FOUR,
                SEVEN
            )
    }

    private fun checkErrors(
        inputValue: String,
        minInputValue: Int,
        maxInputValue: Int,
    ): TextInputState {
        return when {
            (inputValue.toInt() >= minInputValue && inputValue.toInt() <= maxInputValue) && inputValue
                .isNotEmpty() -> TextInputState.Acceptable
            inputValue.isEmpty() -> TextInputState.Default
            else -> {
                TextInputState.Error(
                    messageResId = R.string.edit_text_parameters_error
                )
            }
        }
    }

    companion object {
        private const val MIN_HP_VALUE = 30
        private const val MAX_HP_VALUE = 100
        private const val ONE = 1
        private const val FOUR = 4
        private const val SEVEN = 7
        private const val TWENTY = 20
        private const val THREE = 3

        private const val ATTACK_TEXT_INPUT = 0
        private const val HEALTH_TEXT_INPUT = 1
        private const val PROTECTION_TEXT_INPUT = 2
        private const val MIN_DAMAGE_TEXT_INPUT = 3
        private const val MAX_DAMAGE_TEXT_INPUT = 4
    }
}