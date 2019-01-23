import React from 'react';
import "./SampleApps.css";

const SampleApps = () => {

    const buttons = [ 
        { text: "Core sample app", src: "https://play.google.com/store/apps/details?id=com.pierfrancescosoffritti.aytplayersample" },
        { text: "Chromecast sample app", src: "https://play.google.com/store/apps/details?id=com.pierfrancescosoffritti.cyplayersample" }
    ]

    return (
        <section>
            <div className="section-title">Sample apps</div>
            <div>
                You can download the sample apps from the PlayStore.
                The code is opensource and meant to be used as an example (
                    <a href="https://github.com/PierfrancescoSoffritti/android-youtube-player/tree/master/core-sample-app" target="_blank" rel="noopener noreferrer">core sample app code</a>,&nbsp;
                    <a href="https://github.com/PierfrancescoSoffritti/android-youtube-player/tree/master/chromecast-sender-sample-app" target="_blank" rel="noopener noreferrer">chromecast sample app code</a>).
            </div>
            <div className="play-store-buttons">
                
                { buttons.map( item => 
                    <a className="play-store-button" href={item.src} target="_blank" rel="noopener noreferrer">
                        <svg style={ {width:"32px", height:"32px", marginRight: "8px"} } className="play-store-logo" viewBox="0 0 24 24">
                            <path d="M3,20.5V3.5C3,2.91 3.34,2.39 3.84,2.15L13.69,12L3.84,21.85C3.34,21.6 3,21.09 3,20.5M16.81,15.12L6.05,21.34L14.54,12.85L16.81,15.12M20.16,10.81C20.5,11.08 20.75,11.5 20.75,12C20.75,12.5 20.53,12.9 20.18,13.18L17.89,14.5L15.39,12L17.89,9.5L20.16,10.81M6.05,2.66L16.81,8.88L14.54,11.15L6.05,2.66Z" />
                        </svg>
                        <span>{item.text}</span>
                    </a>
                )}
            </div>
        </section>
    );
}

export default SampleApps