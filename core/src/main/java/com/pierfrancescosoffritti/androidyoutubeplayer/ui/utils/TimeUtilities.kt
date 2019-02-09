package com.pierfrancescosoffritti.androidyoutubeplayer.ui.utils

import android.annotation.SuppressLint

object TimeUtilities {

    /**
     * Transform the time in seconds in a string with format "M:SS".
     */
    @SuppressLint("DefaultLocale")
    fun formatTime(timeInSeconds: Float): String {
        val minutes = (timeInSeconds / 60).toInt()
        val seconds = (timeInSeconds % 60).toInt()
        return String.format("%d:%02d", minutes, seconds)
    }
}
