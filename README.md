# AndroidYouTubePlayer

[![](https://jitpack.io/v/PierfrancescoSoffritti/AndroidYouTubePlayer.svg)](https://jitpack.io/#PierfrancescoSoffritti/AndroidYouTubePlayer)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-AndroidYouTubePlayer-green.svg?style=true)](https://android-arsenal.com/details/1/4322)

The AndroidYouTubePlayer is a simple View that can be easily integrated in every Activity/Fragment. The interaction with YouTube is based on the [IFrame Player API](https://developers.google.com/youtube/iframe_api_reference?hl=it), therefore the YouTube app is not required to use the player.

This library has been developed out of necessity. At the time I was working on an app completely based on the fruition of YouTube videos.
There's an official API provided by Google for the integration of YouTube videos in an Android app: the YouTube Android Player API. But its many bugs and the total lack of support from Google made it impossible to use in production. The app was crashing because of internal bugs of the player ([with 3 years old bug reports](https://code.google.com/p/gdata-issues/issues/detail?id=4395)) and no update has been released in almost a year.

So here it is, the AndroidYouTubePlayer.

Download the sample app [here](https://github.com/PierfrancescoSoffritti/AndroidYouTubePlayer/blob/master/sample/sample-release.apk?raw=true)

Apps using this library: [Shuffly](https://play.google.com/store/apps/details?id=com.pierfrancescosoffritti.shuffly)

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
  compile 'com.github.PierfrancescoSoffritti:AndroidYouTubePlayer:0.7.2'
}
```

## Usage

Add the YouTubePlayerView to your layout
```
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical" >

    <com.pierfrancescosoffritti.youtubeplayer.YouTubePlayerView
        android:id="@+id/youtube_player_view"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"/>
</LinearLayout>
```
Get a reference to the YouTubePlayerView in your code and initialize it
```
YouTubePlayerView youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player_view);
youTubePlayerView.initialize(new AbstractYouTubeListener() {
  @Override
  public void onReady() {
    youTubePlayerView.loadVideo("6JYIGclVQdw", 0);
  }
}, true);
```
The `AbstractYouTubeListener` is just a convenience abstract class, the `initialize` method requires a `YouTubePlayer.YouTubeListener`.

The second parameter is a `boolean`, set it to `true` if you want the `YouTubePlayerView` to handle network events, if you set it to `false` you should handle network events with your broadcast receiver. Reed the doc for more info.

Is possible to listen to specific events such as full-screen on/off, playback events etc.

Use the methods `youTubePlayerView.setCustomActionRight` and `youTubePlayerView.setCustomActionLeft` to add/remove custom actions at the left and right of the play/pause button.
```
youTubePlayerView.setCustomActionRight(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_pause_36dp), new View.OnClickListener() {
  @Override
  public void onClick(View view) {
  }
});
```
if the `OnClickListener` is `null` the custom action will be invisible.
