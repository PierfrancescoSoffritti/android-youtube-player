package com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners

import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer

interface YouTubePlayerInitListener {
    fun onInitSuccess(youTubePlayer: YouTubePlayer)
}