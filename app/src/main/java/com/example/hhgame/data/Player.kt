package com.example.hhgame.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Player(
    override val attack: Int,
    override val protection: Int,
    override var health: Int,
    var maxHealth: Int,
    override val minDamage: Int,
    override val maxDamage: Int,
) : Creature(
    attack,
    protection,
    health,
    minDamage,
    maxDamage,
), Parcelable

