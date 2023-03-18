package com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

internal object Utils {
  fun readHTMLFromUTF8File(inputStream: InputStream): String {
    try {
      val bufferedReader = BufferedReader(InputStreamReader(inputStream, "utf-8"))

      var currentLine: String? = bufferedReader.readLine()
      val sb = StringBuilder()

      while (currentLine != null) {
        sb.append(currentLine).append("\n")
        currentLine = bufferedReader.readLine()
      }

      return sb.toString()
    } catch (e: Exception) {
      throw RuntimeException("Can't parse HTML file.")
    } finally {
      inputStream.close()
    }
  }
}
