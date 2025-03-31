package com.pierfrancescosoffritti.androidyoutubeplayer.core.player

/**
 * A callback that accepts a Boolean value.
 *
 * This interface is only required to support Java 7 and below.
 */
fun interface BooleanProvider {
    fun accept(value: Boolean)
}
