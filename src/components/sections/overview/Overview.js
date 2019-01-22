import React from 'react';
import "./Overview.css"

const Overview = () => {
    return (
        <section>
            <div>
                android-youtube-player is a stable and customizable open source YouTube player for Android. It provides a simple View that can be easily integrated in every Activity/Fragment.
                The interaction with YouTube is based on the IFrame Player API, running inside of a WebView, therefore the YouTube app is not required on the user's device.
                The web UI of the IFrame Player player is hidden. Instead, a native UI built on top of Android is used to interact with the player, providing a native experience to the users.
                The UI of the player is 100% customizable. The default UI can be changed, to show and hide new views, or can be completely replaced by a custom UI.
                This library also provides a Chromecast YouTube player, that you can use to cast YouTube videos from your app to a Chromecast device.
            </div>
            <div className="section-title">Why should you use it</div>
            <div>
                This library has been developed out of necessity. The official library provided by Google to integrate YouTube videos in Android apps is the YouTube Android Player API. I've found the official library to be quite buggy (some bugs are 5+ years old) and lacking in support from Google. It was quite unreliable and therefore unusable in production.
                This, added to its limited options for customization and lack of Chromecast support, lead me to the development of this open source library.
                A lengthier explanation to why you may want to consider using an alternative to the official YouTube player is written in this Medium post.
            </div>
        </section>
    );
}

export default Overview