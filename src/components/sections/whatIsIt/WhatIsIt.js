import React from 'react';
import "./WhatIsIt.css"

const WhatIsIt = () => {
    return (
        <section>
            <div className="section-title">What is it</div>
            <div className="text">
                android-youtube-player is a stable and customizable open source YouTube player for Android. It provides a simple View that can be easily integrated in every Activity/Fragment.<br/><br/>
                To interact with YouTube the library uses the <a href="https://developers.google.com/youtube/iframe_api_reference" target="_blank" rel="noopener noreferrer">IFrame Player API</a>, inside of a WebView, therefore the YouTube app is not required on the user's device.<br/><br/>
                The web UI of the IFrame Player is hidden. Instead, a native UI built on top of Android is used to interact with the player, providing a native experience to the users.<br/>
                The UI of the player is 100% customizable. <a href="https://github.com/PierfrancescoSoffritti/android-youtube-player#playeruicontroller" target="_blank" rel="noopener noreferrer">The default UI can be changed</a>, to show and hide views, or can be <a href="https://github.com/PierfrancescoSoffritti/android-youtube-player#create-your-own-custom-ui" target="_blank" rel="noopener noreferrer">completely replaced by a custom UI</a>.<br/><br/>
                This library also provides a <a href="https://github.com/PierfrancescoSoffritti/android-youtube-player#chromecast-extension-library" target="_blank" rel="noopener noreferrer">Chromecast YouTube player extension</a>, that can be used to cast YouTube videos from an Android app to a Chromecast device.
            </div>
        </section>
    );
}

export default WhatIsIt