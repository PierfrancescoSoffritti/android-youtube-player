package com.pierfrancescosoffritti.androidyoutubeplayer.core.sampleapp.utils;

import java.util.Random;

public class VideoIdsProvider {
  private static final String[] videoIds = {"6JYIGclVQdw", "LvetJ9U_tVY", "S0Q4gqBUs7c", "zOa-rSM4nms"};
  private static final String[] liveVideoIds = {"hHW1oY26kxQ"};
  private static final Random random = new Random();

  public static String getNextVideoId() {
    return videoIds[random.nextInt(videoIds.length)];
  }

  public static String getNextLiveVideoId() {
    return liveVideoIds[random.nextInt(liveVideoIds.length)];
  }
}
