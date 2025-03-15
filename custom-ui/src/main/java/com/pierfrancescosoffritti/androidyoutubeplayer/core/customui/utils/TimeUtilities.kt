package com.pierfrancescosoffritti.androidyoutubeplayer.core.customui.utils

import android.annotation.SuppressLint

object TimeUtilities {

  /**
   * Transform the time in seconds in a string with format "M:SS".
   */
  @SuppressLint("DefaultLocale")
  @JvmStatic
  fun formatTime(timeInSeconds: Float): String {
      val hours = (timeInSeconds / 3600).toInt()
      val minutes = (timeInSeconds % 3600 / 60).toInt()
      val seconds = (timeInSeconds % 60).toInt()
      return if (hours == 0) {
          String.format("%02d:%02d", minutes, seconds)
      } else {
          String.format("%02d:%02d:%02d", hours, minutes, seconds)
      }
  }
}
