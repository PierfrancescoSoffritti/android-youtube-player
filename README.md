# Android-YouTube-Player

[![](https://jitpack.io/v/PierfrancescoSoffritti/AndroidYouTubePlayer.svg)](https://jitpack.io/#PierfrancescoSoffritti/AndroidYouTubePlayer)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Android--YouTube--Player-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/4322)

The Android-YouTube-Player library provides a simple View that can be easily integrated in every Activity/Fragment. The interaction with YouTube is based on the [IFrame Player API](https://developers.google.com/youtube/iframe_api_reference?hl=it), running inside of a WebView, therefore the YouTube app is not required to use this player.
The web UI of the IFrame Player player is hidden, instead a native UI built on top of Android is used to interact with the player, providing a native experience to the users.

## Why does this library exists?

This library has been developed out of necessity. The official library provided by Google for the integration of YouTube videos into Android apps is the [YouTube Android Player API](https://developers.google.com/youtube/android/player/). Its many bugs and the total lack of support from Google made it impossible to use in production.
Originally I have tried to use the official library, but my app was crashing because of internal bugs in Google's player, ([some bugs have 3+ years old bug reports](https://code.google.com/p/gdata-issues/issues/detail?id=4395)) those bugs haven't been fixed for years. From there I decided to build my own player.

More reasons why you may want to consider using an alternative to the official YouTube player are written in [this post](https://medium.com/@soffritti.pierfrancesco/how-to-play-youtube-videos-in-your-android-app-c40427215230).

---

**This library has a [Wiki](https://github.com/PierfrancescoSoffritti/Android-YouTube-Player/wiki/Quick-start), check it out!**

If this library is useful to you, **consider leaving a star on GitHub** :)

A list of published apps that are using this library: ([let me know](https://github.com/PierfrancescoSoffritti/Android-YouTube-Player/issues) if you want to add your app to this list)

- [Shuffly](https://play.google.com/store/apps/details?id=com.pierfrancescosoffritti.shuffly)

<img height="450" src="https://github.com/PierfrancescoSoffritti/AndroidYouTubePlayer/blob/master/pics/ayp.gif" />

## Download
Add this to your project-level `build.gradle`:
```
allprojects {
  repositories {
    ...
    maven { url "https://jitpack.io" }
  }
}
```
Add this to your module-level `build.gradle`:
```
dependencies {
  compile 'com.github.PierfrancescoSoffritti:AndroidYouTubePlayer:4.1.4'
}
```

## Proguard
If you are using ProGuard you might need to add the following option:
```
-keep public class com.pierfrancescosoffritti.youtubeplayer.** {
   public *;
}

-keepnames class com.pierfrancescosoffritti.youtubeplayer.*
```
## Usage

A sample project that shows how to use the library is available in the [sample module](https://github.com/PierfrancescoSoffritti/Android-YouTube-Player/tree/master/sample). You can also [download the sample apk here](https://github.com/PierfrancescoSoffritti/Android-YouTube-Player/tree/master/sample/apk).

**Please refer to the [Wiki](https://github.com/PierfrancescoSoffritti/Android-YouTube-Player/wiki/Quick-start) of the library for a detailed description on how to use it.**

### Quick start

In order to start using the player you need to add the [YouTubePlayerView](https://github.com/PierfrancescoSoffritti/Android-YouTube-Player/wiki/YouTubePlayerView) to your layout
```
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical" >

    <com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerView
        android:id="@+id/youtube_player_view"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"/>
</LinearLayout>
```
Get a reference to the `YouTubePlayerView` in your code and initialize it
```
YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);

youTubePlayerView.initialize(new YouTubePlayerInitListener() {
    @Override
    public void onInitSuccess(final YouTubePlayer initializedYouTubePlayer) {
    
        initializedYouTubePlayer.addListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady() {
                String videoId = "6JYIGclVQdw";
                initializedYouTubePlayer.loadVideo(videoId, 0);
            }
        });
        
    }
}, true);
```

More info on the initialization method can be found [here](https://github.com/PierfrancescoSoffritti/Android-YouTube-Player/wiki/YouTubePlayerView#initialization).

The [AbstractYouTubePlayerListener](https://github.com/PierfrancescoSoffritti/Android-YouTube-Player/blob/master/YouTubePlayer/src/main/java/com/pierfrancescosoffritti/youtubeplayer/player/AbstractYouTubePlayerListener.java) is just a convenience abstract class that implements `YouTubePlayerListener`, so that is not necessary to always implement all the methods of the interface.

The playback of the videos is handled by the [YouTubePlayer](https://github.com/PierfrancescoSoffritti/Android-YouTube-Player/wiki/YouTubePlayer). You must use that for everything concerning video playback.

The UI of the player is handled by a [PlayerUIController](https://github.com/PierfrancescoSoffritti/Android-YouTube-Player/wiki/PlayerUIController), in order to interact with it you must get its reference from the `YouTubePlayerView`

```
PlayerUIController uiController = youTubePlayerView.getPlayerUIController();
```
