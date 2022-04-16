package com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.utils

import android.annotation.SuppressLint

object TimeUtilities {

    /**
     * Transform the time in seconds in a string with format "M:SS".
     */
    @SuppressLint("DefaultLocale")
    @JvmStatic
    fun formatTime(timeInSeconds: Float): String {
        var timeString =""
        
        var hours = timeInSeconds / 3600
        var minutes = (timeInSeconds % 3600) / 60
        var seconds = timeInSeconds % 60
       
        
         //formatting hours minutes and seconds in string
        if(hours==0L && minutes==0L){
            timeString = String.format("%d:%02d", minutes, seconds)
        }else if(hours==0L && minutes<10L){
            timeString = String.format("%d:%02d", minutes, seconds)
        }else if(hours==0L && minutes>9L){
            timeString = String.format("%02d:%02d", minutes, seconds)
        }else if(hours<10L && hours>0L){
            timeString = String.format("%d:%02d:%02d", hours, minutes, seconds)
        }else if(hours>9L){
            timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds)
        }

        
        return timeString
    }
}
