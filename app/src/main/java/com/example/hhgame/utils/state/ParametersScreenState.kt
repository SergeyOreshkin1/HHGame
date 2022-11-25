package com.example.hhgame.utils.state

data class ParametersScreenState (
    val attack: TextInputState = TextInputState.Default,
    val protection: TextInputState = TextInputState.Default,
    val health: TextInputState = TextInputState.Default,
    val minDamage: TextInputState = TextInputState.Default,
    val maxDamage: TextInputState = TextInputState.Default,
) {
    val isAcceptable: Boolean =
        attack is TextInputState.Acceptable
                && protection is TextInputState.Acceptable
                && health is TextInputState.Acceptable
                && minDamage is TextInputState.Acceptable
                && maxDamage is TextInputState.Acceptable
}