# Android-YouTube-Player

[![](https://jitpack.io/v/PierfrancescoSoffritti/AndroidYouTubePlayer.svg)](https://jitpack.io/#PierfrancescoSoffritti/AndroidYouTubePlayer)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Android--YouTube--Player-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/4322)

The AndroidYouTubePlayer is a simple View that can be easily integrated in every Activity/Fragment. The interaction with YouTube is based on the [IFrame Player API](https://developers.google.com/youtube/iframe_api_reference?hl=it), therefore the YouTube app is not required to use the player.

## Why does this library exists?

This library has been developed out of necessity. The official library provided by Google for the integration of YouTube videos into Android apps is the [YouTube Android Player API](https://developers.google.com/youtube/android/player/). Its many bugs and the total lack of support from Google made it impossible to use in production. I've tried to use it but my app was crashing because of internal bugs in Google's player, ([sone bugs have 3+ years old bug reports](https://code.google.com/p/gdata-issues/issues/detail?id=4395)) and, at the moment, no update has been released for almost a year. This library provides a stable and open source alternative to the official YouTube Player.

Apps using this library: (send me an email if you want to add your app to the list)

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
  compile 'com.github.PierfrancescoSoffritti:AndroidYouTubePlayer:1.0.0'
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

A sample project that shows how to use the library is available in the [sample module](https://github.com/PierfrancescoSoffritti/Android-YouTube-Player/tree/master/sample). You can also [download the sample apk here](https://github.com/PierfrancescoSoffritti/AndroidYouTubePlayer/blob/master/sample/sample-release.apk?raw=true).

**Please refer to the [Wiki](https://github.com/PierfrancescoSoffritti/Android-YouTube-Player/wiki/Quick-start) of the library for a detailed description on how to use it.**

# Quick start

In order to start using the player you need to add the YouTubePlayerView to your layout
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
Get a reference to the YouTubePlayerView in your code and initialize it
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

The second parameter of the `initialize` method is a `boolean`, set it to `true` if you want the `YouTubePlayerView` to handle network events, if you set it to `false` you should handle network events with your own broadcast receiver.

The `AbstractYouTubePlayerListener` is just a convenience abstract class implementing `YouTubePlayerListener`, so that is not necessary to implement all the methods of the interface.

The playback of the videos is handled by the `YouTubePlayer`. You must use that for everything concerning the playback.

The UI of the player is handled by a `PlayerUIController`, in order to interact with it you must get its reference from the `YouTubePlayerView`

```
PlayerUIController uiController = youTubePlayerView.getPlayerUIController();
```

Use the methods `uiController.setCustomAction1` and `uiController.setCustomAction2` to add/remove custom actions at the left and right of the play/pause button.

```
uiController.setCustomAction1(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_pause_36dp), new View.OnClickListener() {
    @Override
    public void onClick(View view) {
    }
});
```
