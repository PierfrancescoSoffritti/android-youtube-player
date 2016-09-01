package com.pierfrancescosoffritti.youtubeplayer;

public class Utils {

    public static String formatTime(float sec) {
        int minutes = (int) (sec / 60);
        int seconds = (int) (sec % 60);
        return String.format("%d:%02d", minutes, seconds);
    }
}
