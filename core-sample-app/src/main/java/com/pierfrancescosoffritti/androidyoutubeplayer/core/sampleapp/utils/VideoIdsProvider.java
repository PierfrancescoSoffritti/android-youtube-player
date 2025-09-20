package com.pierfrancescosoffritti.androidyoutubeplayer.core.sampleapp.utils;

import java.util.Random;

public class VideoIdsProvider {
  private static final String[] videoIds = {"-e_3Cg9GZFU", "LvetJ9U_tVY", "S0Q4gqBUs7c", "kqSdQq5bklE", "n365C9NbbC4"};
  private static final String[] liveVideoIds = {"hHW1oY26kxQ"};
  private static final Random random = new Random();

  public static String getNextVideoId() {
    return videoIds[random.nextInt(videoIds.length)];
  }

  public static String getNextLiveVideoId() {
    return liveVideoIds[random.nextInt(liveVideoIds.length)];
  }
}
