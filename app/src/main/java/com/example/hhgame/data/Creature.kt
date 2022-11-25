package com.example.hhgame.data

abstract class Creature(
    open val attack: Int,
    open val protection: Int,
    open var health: Int,
    open val minDamage: Int,
    open val maxDamage: Int,
) {

    fun calculateAttackModifier(attackersAttack: Int, defenderDefense: Int): Int {
        val result: Int
        val attackModifier = (attackersAttack - defenderDefense) + 1
        result = if (attackModifier <= 0) MIN_ATTACK_MODIFIER_VALUE else attackModifier
        return result
    }

    companion object {
        private const val MIN_ATTACK_MODIFIER_VALUE = 1
    }
}