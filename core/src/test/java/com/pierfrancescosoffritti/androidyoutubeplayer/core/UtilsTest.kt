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
        // Prepare
        val html = "<div>some<span>fake\u0000</span>html</div>\n<div>臺some<span>fake\u0007</span>html</div>"
        val inputStream = html.byteInputStream(StandardCharsets.UTF_8)

        // Act
        val string = Utils.readHTMLFromUTF8File(inputStream)

        // Assert
        assertEquals(string, html+"\n")
    }
}
