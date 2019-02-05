import React from 'react';
import "./Differences.css"

const Differences = () => {
    return (
        <section>
            <div className="section-title">Differences from the official API</div>
            <div>
                The official library provided by Google to integrate YouTube videos in Android apps is the YouTube Android Player API.<br/>
                The YouTube Android Player API is quite buggy (<a href="https://issuetracker.google.com/issues/35172585" target="_blank" rel="noopener noreferrer">some bugs are 5+ years old</a>) and lacking in support from Google. I found it to be quite unreliable and therefore unusable in production.<br/><br/>
                This, added to its limited options for customization and lack of Chromecast support, lead to the development of this open source library.<br/><br/>
                A lengthier explanation about why you may want to consider using an alternative to the official YouTube player is written <a href="https://medium.com/@soffritti.pierfrancesco/how-to-play-youtube-videos-in-your-android-app-c40427215230" target="_blank" rel="noopener noreferrer">in this Medium post</a>.
            </div>
        </section>
    );
}

export default Differences