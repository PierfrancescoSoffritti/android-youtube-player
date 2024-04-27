package com.pierfrancescosoffritti.androidyoutubeplayer.core

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.getVideoIdFromUrl
import org.junit.Test

class UrlHelperTest {

    @Test
    fun testGetVideoIdFromUrl_WithUrlHasIdAndUrlSeparator_ReturnsVideoId() {
        val expected = getVideoIdFromUrl("https://www.youtube.com/watch?v=FBMAlNURLI4&t=44s")

        assert(expected == "FBMAlNURLI4")
    }

    @Test
    fun testGetVideoIdFromUrl_WithUrlHasId_ReturnsVideoId() {
        val expected = getVideoIdFromUrl("https://www.youtube.com/watch?v=FBMAlNURLI4")

        assert(expected == "FBMAlNURLI4")
    }

    @Test
    fun testGetVideoIdFromUrl_WithInvalidUrl_ReturnsNull() {
        val expected = getVideoIdFromUrl("https://www.youtube.com/watch")

        assert(expected == null)
    }
}