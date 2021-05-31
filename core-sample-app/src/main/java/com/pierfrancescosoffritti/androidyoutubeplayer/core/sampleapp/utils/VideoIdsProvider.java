package com.pierfrancescosoffritti.androidyoutubeplayer.core.sampleapp.utils;

import java.util.Random;

public class VideoIdsProvider {
    private static String[] videoIds = {"6JYIGclVQdw", "LvetJ9U_tVY", "S0Q4gqBUs7c", "zOa-rSM4nms"};
    private static String[] liveVideoIds = {"hHW1oY26kxQ"};
    private static int currentIndex = 0;

    public static String getNextVideoId() {
        String id = videoIds[currentIndex];
        return id;
    }

    public static String getNextLiveVideoId() {
        String id = liveVideoIds[currentIndex];
        return id;
    }

    public static void incrementIndex(){
        if(currentIndex >= (videoIds.length-1)) {
            currentIndex = 0;
        }
        else{
            currentIndex++;
        }
    }

    public static void decrementIndex(){
        if(currentIndex > 0) currentIndex --;
    }
}
