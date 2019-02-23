package com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.utils

import java.util.*

object VideoIdsProvider {
    private val videoIds = arrayOf("6JYIGclVQdw", "LvetJ9U_tVY", "S0Q4gqBUs7c", "zOa-rSM4nms")
    private val random = Random()

    fun getNextVideoId(): String {
        return videoIds[random.nextInt(videoIds.size)]
    }
}