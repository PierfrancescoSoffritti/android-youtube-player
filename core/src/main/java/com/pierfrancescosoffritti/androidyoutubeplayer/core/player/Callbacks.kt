package com.pierfrancescosoffritti.androidyoutubeplayer.core.player

/**
 * A callback accepting a Boolean value
 */
fun interface BooleanProvider {
    fun accept(value: Boolean)
}