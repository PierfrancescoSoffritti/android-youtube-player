import React from 'react';
import "./Differences.css"

const Differences = () => {
    return (
        <section>
            <div className="section-title">Why does this library exist?</div>
            <div>
            The library provided by Google is the YouTube Android Player API.
            This library <a href="https://issuetracker.google.com/issues/35172585" target="_blank" rel="noopener noreferrer">has been historically not reliable</a> and is now deprecated by Google.
            <br/><br/>
            Google now recommends using the IFrame Player API inside a WebView, which is exactly what this library does, while also providing a native Java/Kotlin interface to interact with the web player.
            <br/><br/>
            A lengthier explanation of why this library was created can be found <a href="https://medium.com/@soffritti.pierfrancesco/how-to-play-youtube-videos-in-your-android-app-c40427215230" target="_blank" rel="noopener noreferrer">in this Medium post</a>.
            </div>
        </section>
    );
}

export default Differences