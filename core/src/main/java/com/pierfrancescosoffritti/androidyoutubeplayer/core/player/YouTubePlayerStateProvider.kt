package com.pierfrancescosoffritti.androidyoutubeplayer.core.player

interface YouTubePlayerStateProvider {
    /** Gets the current mute state of the player. Suspending function for Kotlin coroutine use. */
    suspend fun isMuted(): Boolean

    /** Provides the current mute state of the player via callback for Java compatibility. */
    fun isMuted(callback: (Boolean) -> Unit)
}