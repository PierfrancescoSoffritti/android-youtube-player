import React from 'react';
import "./Header.css"

const Header = () => {
    return (
        <header className="header header-dim">
            <div className="logo logo-dim" />
            <div className="description">
                <div>YouTube Player library for Android, stable and customizable.</div>
                <div>android-youtube-player is the easiest way to play YouTube videos in your app and to cast YouTube videos to a Chromecast.</div>
            </div>
        </header>
    );
}

export default Header