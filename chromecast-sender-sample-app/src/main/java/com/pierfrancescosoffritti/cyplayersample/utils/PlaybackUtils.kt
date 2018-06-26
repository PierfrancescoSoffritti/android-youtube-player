package com.pierfrancescosoffritti.cyplayersample.utils

import java.util.*

object PlaybackUtils {
    private val videoIds = arrayOf("6JYIGclVQdw", "LvetJ9U_tVY", "S0Q4gqBUs7c", "zOa-rSM4nms")

    fun getNextVideoId(): String {
        return videoIds[Random().nextInt(videoIds.size)]
    }
}