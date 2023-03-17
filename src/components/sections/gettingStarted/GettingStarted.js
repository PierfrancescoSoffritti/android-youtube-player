import React from 'react';
import "./GettingStarted.css";

import SyntaxHighlighter from 'react-syntax-highlighter';
import { agate  } from 'react-syntax-highlighter/dist/styles/hljs';

const GettingStarted = () => {
    return (
        <section>
            <div className="section-title">Getting started</div>
            <div>

In order to start using the player you need to add a YouTubePlayerView to your layout.

<SyntaxHighlighter language='xml' style={ agate }>{
`<LinearLayout
  xmlns:android="http://schemas.android.com/apk/res/android"
  xmlns:app="http://schemas.android.com/apk/res-auto"
  android:layout_width="match_parent"
  android:layout_height="match_parent"
  android:orientation="vertical" >

  <com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
    android:id="@+id/youtube_player_view"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    
    app:videoId="S0Q4gqBUs7c"
    app:autoPlay="true" />

</LinearLayout>`
}</SyntaxHighlighter>

It is recommended that you add YouTubePlayerView as a lifecycle observer of its parent Activity/Fragment. You can <a href="https://github.com/PierfrancescoSoffritti/android-youtube-player#lifecycleobserver" target="_blank" rel="noopener">read why in the documentation</a>.

<SyntaxHighlighter language='java' style={ agate }>{
`YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);
getLifecycle().addObserver(youTubePlayerView);`
}</SyntaxHighlighter>

<i>(If you have problems adding YouTubePlayerView as a LifecycleObserver, you probably aren't using androidx, <a href="https://developer.android.com/jetpack/androidx/migrate" target="_blank" rel="noopener">I suggest you migrate your dependencies</a>)</i>
<br/><br/>
That's all you need, a YouTube video is now playing in your app.
<br/><br/>
If you want more control, everything can be done programmatically by getting a reference to your YouTubePlayerView and adding a YouTubePlayerListener to it.

<SyntaxHighlighter language='java' style={ agate }>{
`YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);
getLifecycle().addObserver(youTubePlayerView);

youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
  @Override
  public void onReady(@NonNull YouTubePlayer youTubePlayer) {
    String videoId = "S0Q4gqBUs7c";
    youTubePlayer.loadVideo(videoId, 0);
  }
});`
}</SyntaxHighlighter>

You can <a href="https://github.com/PierfrancescoSoffritti/android-youtube-player#table-of-contents-core" target="_blank" rel="noopener">read the complete documentation here</a>.
<br/><br/>
For any question feel free to <a href="https://github.com/PierfrancescoSoffritti/android-youtube-player/issues" target="_blank" rel="noopener">open an issue on GitHub</a>.

            </div>
        </section>
    );
}

export default GettingStarted