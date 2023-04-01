package com.pierfrancescosoffritti.androidyoutubeplayer.core

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.readHTMLFromUTF8File
import org.junit.Assert.assertEquals
import org.junit.Test
import java.nio.charset.StandardCharsets

class UtilsTest {
  @Test
  fun readParseHtmlCorrectly() {
    val html = "<div>some<span>fake</span>html</div>\n<div>some<span>fake</span>html</div>"
    val inputStream = html.byteInputStream(StandardCharsets.UTF_8)

    val parsedHtml = readHTMLFromUTF8File(inputStream)

    assertEquals(parsedHtml, html)
  }
}
