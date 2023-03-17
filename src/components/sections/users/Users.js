import React from 'react';
import "./Users.css"

const Users = () => {
    return (
        <section>
            <div className="section-title">Who is using this library</div>
            <div>
            Now that the official API from Google is deprecated, android-youtube-player is the main YouTube player library for Android.
            <br/><br/>
            <b>Used by over 5 thousands apps</b>, with some big names like <a href="https://play.google.com/store/apps/details?id=com.flipkart.android" target="_blank" rel="noopener">Flipkart</a>, <a href="https://play.google.com/store/apps/details?id=com.mcdo.mcdonalds" target="_blank" rel="noopener">McDonald's</a>, <a href="https://play.google.com/store/apps/details?id=com.camerasideas.instashot" target="_blank" rel="noopener">InShot Video Editor</a>, <a href="https://play.google.com/store/apps/details?id=com.genius.android" target="_blank" rel="noopener">Genius</a> and <a href="https://play.google.com/store/apps/details?id=com.andrewshu.android.reddit" target="_blank" rel="noopener">reddit is fun</a>.
            <br/>
            <a href="https://www.appbrain.com/stats/libraries/details/android_youtube_player/android-youtube-player" target="_blank" rel="noopener">You can see more stats here</a>.
            <br/><br/>
            If you choose to use this library and profit from it, consider informing me and <a href="https://github.com/sponsors/PierfrancescoSoffritti" target="_blank" rel="noopener">become a sponsor on GitHub</a>. This will enable me to continue developing the library, so you don't have to.
            </div>
        </section>
    );
}

export default Users