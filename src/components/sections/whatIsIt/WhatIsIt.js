import React from 'react';
import "./WhatIsIt.css"

const WhatIsIt = () => {
    return (
        <section>
            <div className="section-title">What is it</div>
            <div className="text">
                android-youtube-player is a stable and customizable open source YouTube player for Android. It provides a simple View that can be easily integrated in every Activity/Fragment.<br/><br/>
                The library is a wrapper over the <a href="https://developers.google.com/youtube/iframe_api_reference" target="_blank" rel="noopener noreferrer">IFrame Player API</a>, which runs inside of a WebView.<br/><br/>
                The UI of the player is 100% customizable. The UI can be <a href="https://github.com/PierfrancescoSoffritti/android-youtube-player#create-your-own-custom-ui" target="_blank" rel="noopener noreferrer">completely replaced by a custom UI</a>.<br/><br/>
                This library also provides a <a href="https://github.com/PierfrancescoSoffritti/android-youtube-player#chromecast-extension-library" target="_blank" rel="noopener noreferrer">Chromecast YouTube player extension</a>, that can be used to cast YouTube videos from an Android app to a Chromecast device.
            </div>
        </section>
    );
}

export default WhatIsIt