package com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils

/**
 * Extracts the video ID from a YouTube URL.
 *
 * @param url The YouTube URL.
 * @return The video ID, or the original URL if the video ID cannot be found.
 */
fun getVideoIdFromUrl(url: String): String? {
    val startIndex = url.indexOf("?v=")

    if (startIndex == -1) {
        return null
    }

    val linkParamSymbolIndex = url.indexOf("&")

    val lastIndex = if (linkParamSymbolIndex == -1) {
        url.length
    } else {
        linkParamSymbolIndex
    }

    return url.substring(startIndex + 3, lastIndex)
}