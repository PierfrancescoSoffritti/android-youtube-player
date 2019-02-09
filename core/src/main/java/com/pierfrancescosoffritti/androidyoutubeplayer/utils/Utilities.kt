package com.pierfrancescosoffritti.androidyoutubeplayer.utils

import android.annotation.SuppressLint

object Utilities {
    @SuppressLint("DefaultLocale")
    fun formatTime(timeInSeconds: Float): String {
        val minutes = (timeInSeconds / 60).toInt()
        val seconds = (timeInSeconds % 60).toInt()
        return String.format("%d:%02d", minutes, seconds)
    }
}
