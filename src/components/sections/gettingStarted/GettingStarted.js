import React from 'react';
import "./GettingStarted.css";

import SyntaxHighlighter from 'react-syntax-highlighter';
import { agate  } from 'react-syntax-highlighter/dist/styles/hljs';

const GettingStarted = () => {
    return (
        <section>
            <div className="section-title">Getting started</div>
            <div>

In order to start using the player you need to add a YouTubePlayerView to your layout:

<SyntaxHighlighter language='xml' style={ agate }>{
`<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical" >

    <com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView
        android:id="@+id/youtube_player_view"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"/>
</LinearLayout>`
}</SyntaxHighlighter>

Get a reference to the YouTubePlayerView in your Activity/Fragment and initialize it:

<SyntaxHighlighter language='java' style={ agate }>{
`YouTubePlayerView youtubePlayerView = findViewById(R.id.youtube_player_view);
getLifecycle().addObserver(youtubePlayerView);

youtubePlayerView.initialize(new YouTubePlayerInitListener() {
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
}, true);`
}</SyntaxHighlighter>

This is all you need, a YouTube video is now playing in your app.
<br/><br/>
You can <a href="">read the complete documentation here</a>.

            </div>
        </section>
    );
}

export default GettingStarted