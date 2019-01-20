import React from 'react';
import "./Header.css"

const Header = () => {
    return (
        <header className="header header-dim">
            
            <canvas className="background header-dim" />

            <img 
                className="logo-dim"
                src="https://firebasestorage.googleapis.com/v0/b/androidyoutubeplayer-sampleapp.appspot.com/o/android-youtube-player-logo.webp?alt=media&token=12ad47c4-52a5-43c5-88eb-0fe39cc5078c"
                alt="logo"
            />
            <div className="description">
                <div>YouTube Player library for Android, stable and customizable.</div>
                <div>android-youtube-player is the easiest way to play YouTube videos in your app and to cast YouTube videos to a Chromecast.</div>
            </div>
        </header>
    );
}

export default Header