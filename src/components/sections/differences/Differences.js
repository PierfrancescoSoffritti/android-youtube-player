import React from 'react';
import "./Differences.css"

const Differences = () => {
    return (
        <section>
            <div className="section-title">Differences from the official player API</div>
            <div>
                The official library provided by Google to integrate YouTube videos in Android apps is the YouTube Android Player API. The official library to be quite buggy (some bugs are 5+ years old) and lacking in support from Google. It was quite unreliable and therefore unusable in production.<br/><br/>
                This, added to its limited options for customization and lack of Chromecast support, lead to the development of this open source library.<br/><br/>
                A lengthier explanation to why you may want to consider using an alternative to the official YouTube player is written in this Medium post.
            </div>
        </section>
    );
}

export default Differences