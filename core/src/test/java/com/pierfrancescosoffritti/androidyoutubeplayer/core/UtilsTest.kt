package com.pierfrancescosoffritti.androidyoutubeplayer.core

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.Utils
import org.junit.Assert.assertEquals
import org.junit.Test
import java.nio.charset.StandardCharsets

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class UtilsTest {
    @Test
    fun readHTMLFromUTF8File_isCorrect() {
        // ARRANGE
        val html = "<div>some<span>fake</span>html</div>\n<div>some<span>fake</span>html</div>"
        val inputStream = html.byteInputStream(StandardCharsets.UTF_8)

        // ACT
        val string = Utils.readHTMLFromUTF8File(inputStream)

        // ASSERT
        assertEquals(string, html+"\n")
    }
}
