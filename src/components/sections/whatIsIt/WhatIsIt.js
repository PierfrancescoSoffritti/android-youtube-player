import React from 'react';
import "./WhatIsIt.css"

const WhatIsIt = () => {
    return (
        <section>
            <div className="section-title">What is it</div>
            <div className="text">
                android-youtube-player is a stable and customizable open source YouTube player for Android. It provides a simple View that can be easily integrated in every Activity/Fragment.<br/><br/>
                To interact with YouTube the library uses the IFrame Player API, inside of a WebView, therefore the YouTube app is not required on the user's device.<br/><br/>
                The web UI of the IFrame Player player is hidden. Instead, a native UI built on top of Android is used to interact with the player, providing a native experience to the users.<br/>
                The UI of the player is 100% customizable. The default UI can be changed, to show and hide new views, or can be completely replaced by a custom UI.<br/><br/>
                This library also provides a Chromecast YouTube player, that you can use to cast YouTube videos from your app to a Chromecast device.
            </div>
        </section>
    );
}

export default WhatIsIt