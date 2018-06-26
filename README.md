# Android-YouTube-Player
[![](https://jitpack.io/v/PierfrancescoSoffritti/AndroidYouTubePlayer.svg)](https://jitpack.io/#PierfrancescoSoffritti/AndroidYouTubePlayer)
[![](https://img.shields.io/badge/Android%20Arsenal-Android--YouTube--Player-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/4322)

[![](https://img.shields.io/twitter/url/http/shields.io.svg?style=social)](https://twitter.com/intent/tweet?text=A%20new%20YouTube%20Player%20library%20for%20Android,%20stable%20and%20customizable&url=https://github.com/PierfrancescoSoffritti/Android-YouTube-Player&via=PierfrancescoSo&hashtags=opensource,youtubeplayer,androiddev)

<img align="right" width="180px" src="https://raw.githubusercontent.com/PierfrancescoSoffritti/Android-YouTube-Player/master/pics/Android-YouTube-Player_512x512.png" title="Android-YouTube-Player logo" />

The Android-YouTube-Player library is a stable and customizable open source YouTube player for Android. It provides a simple View that can be easily integrated in every Activity/Fragment.

The interaction with YouTube is based on the [IFrame Player API](https://developers.google.com/youtube/iframe_api_reference), running inside of a WebView, therefore the YouTube app is not required to be installed on the user's device.

The web UI of the IFrame Player player is hidden, instead a native UI built on top of Android is used to interact with the player, providing a native experience to the users.

## Why does this library exists?
This library has been developed out of necessity. The official library provided by Google to integrate YouTube videos in Android apps is the [YouTube Android Player API](https://developers.google.com/youtube/android/player/). I've found the official library to be quite buggy ([some bugs are 5+ years old](https://code.google.com/p/gdata-issues/issues/detail?id=4395)) and lacking in terms of support from Google. It was quite unreliable and unusable in production.

A lengthier explanation to why you may want to consider using an alternative to the official YouTube player is written in [this Medium post](https://medium.com/@soffritti.pierfrancesco/how-to-play-youtube-videos-in-your-android-app-c40427215230).

---

**This library has a [Wiki](https://github.com/PierfrancescoSoffritti/Android-YouTube-Player/wiki/Quick-start), check it out!**

A list of published apps that are using this library: ([let me know](https://github.com/PierfrancescoSoffritti/Android-YouTube-Player/issues) if you want to add your app to this list)

- [Shuffly](https://play.google.com/store/apps/details?id=com.pierfrancescosoffritti.shuffly)

<img src="https://raw.githubusercontent.com/PierfrancescoSoffritti/Android-YouTube-Player/master/pics/showcase.jpg" />

# Table of Contents (Core)
1. [Sample app](https://github.com/PierfrancescoSoffritti/Android-YouTube-Player#sample-app)
2. [Download](https://github.com/PierfrancescoSoffritti/Android-YouTube-Player#download)
    1. [Core](https://github.com/PierfrancescoSoffritti/Android-YouTube-Player#core)
    2. [Chromecast](https://github.com/PierfrancescoSoffritti/Android-YouTube-Player#chromecast) 
3. [Quick start](https://github.com/PierfrancescoSoffritti/Android-YouTube-Player#quick-start)
4. [YouTubePlayerView]()
    1. Initialization
    2. Full screen
    3. UI
    4. Free resources
5. YouTubePlayer
    1. Events
    2. Player state
6. YouTubePlayerListener
7. PlayerUIController
    1. Show video title
    2. Live videos UI
    3. Custom actions
8. Create your custom UI
9. Menu
    1. YouTubePlayerMenu
    2. DefaultYouTubePlayerMenu
    3. MenuItem
10. Network events
11. Chromecast
12. Utilities
    1. Track the state of a YouTubePlayer object
13. Useful info
    1. Hardware acceleration
    2. Play YouTube videos in the background
    3. minSdk

# Table of Contents (Chromecast)

# Sample app
This repository has two sample modules to show how to use various functionalities of the library. One [sample module for the core library]() and [one sample module for the chromecast extension]().

You can download the apks of the two sample app [here (core)]() and [here (chromecast)](), or on the PlayStore.

core: 
<a href='https://play.google.com/store/apps/details?id=com.pierfrancescosoffritti.aytplayersample&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'>
    <img width='200px' alt='Get it on Google Play'
         src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png'/>
</a>

chromecast:
<a href='https://play.google.com/store/apps/details?id=com.pierfrancescosoffritti.aytplayersample&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'>
    <img width='200px' alt='Get it on Google Play'
         src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png'/>
</a>

Having the sample apps installed is a good way to be notified of new releases. Although Watching this repository will allow GitHub to email you whenever I publish a release.

# Download
The Gradle dependency is available via jCenter. jCenter is the default Maven repository used by Android Studio.

The minimum API level supported by this library is API 17.

### Core
The *core* module contains the YouTube Player. 
```
dependencies {
  implementation 'com.github.PierfrancescoSoffritti:AndroidYouTubePlayer:7.0.1'
}
```

### Chromecast
The *chromecast* module is an extension library for the *core* module. Use this if you need to cast videos from your app to a Chromecast device.
```
dependencies {
  implementation 'com.github.PierfrancescoSoffritti:AndroidYouTubePlayer:7.0.1'
  implementation 'com.github.PierfrancescoSoffritti:AndroidYouTubePlayer:7.0.1'
}
```

# Quick start
In order to start using the player you need to add the [YouTubePlayerView](https://github.com/PierfrancescoSoffritti/Android-YouTube-Player#YouTubePlayerView) to your layout
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
    public void onInitSuccess(@NonNull final YouTubePlayer initializedYouTubePlayer) {    
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
