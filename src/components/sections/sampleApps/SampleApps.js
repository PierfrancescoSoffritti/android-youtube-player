import React from 'react';
import "./SampleApps.css";

const SampleApps = () => {

    const buttons = [ 
        { text: "Core sample app", store: "https://play.google.com/store/apps/details?id=com.pierfrancescosoffritti.aytplayersample", github: "https://github.com/PierfrancescoSoffritti/android-youtube-player/tree/master/core-sample-app" },
        { text: "Chromecast sample app", store: "https://play.google.com/store/apps/details?id=com.pierfrancescosoffritti.cyplayersample", github: "https://github.com/PierfrancescoSoffritti/android-youtube-player/tree/master/chromecast-sender-sample-app" }
    ]

    return (
        <section>
            <div className="section-title">Sample apps</div>
            <div>
                You can download the sample apps from the PlayStore. The code is opensource and meant to be used as an example.
            </div>
            <div className="buttons-container">                
                { buttons.map( (item, idx) => 
                    <a key={idx} className="play-store-button" href={item.store} target="_blank" rel="noopener noreferrer">
                        <svg style={ {width:"32px", height:"32px", marginRight: "8px"} } className="play-store-logo" viewBox="0 0 24 24">
                            <path d="M3,20.5V3.5C3,2.91 3.34,2.39 3.84,2.15L13.69,12L3.84,21.85C3.34,21.6 3,21.09 3,20.5M16.81,15.12L6.05,21.34L14.54,12.85L16.81,15.12M20.16,10.81C20.5,11.08 20.75,11.5 20.75,12C20.75,12.5 20.53,12.9 20.18,13.18L17.89,14.5L15.39,12L17.89,9.5L20.16,10.81M6.05,2.66L16.81,8.88L14.54,11.15L6.05,2.66Z" />
                        </svg>
                        <span>{item.text}</span>
                    </a>
                )}                
            </div>
            <div className="buttons-container">
                { buttons.map( (item, idx) => 
                    <a key={idx} className="sample-app-button" href={item.github} target="_blank" rel="noopener noreferrer">
                        <svg style={ {width:"32px", height:"32px", marginRight: "8px"} } className="sample-app-logo" viewBox="0 0 24 24">
                            <path d="M12,2A10,10 0 0,0 2,12C2,16.42 4.87,20.17 8.84,21.5C9.34,21.58 9.5,21.27 9.5,21C9.5,20.77 9.5,20.14 9.5,19.31C6.73,19.91 6.14,17.97 6.14,17.97C5.68,16.81 5.03,16.5 5.03,16.5C4.12,15.88 5.1,15.9 5.1,15.9C6.1,15.97 6.63,16.93 6.63,16.93C7.5,18.45 8.97,18 9.54,17.76C9.63,17.11 9.89,16.67 10.17,16.42C7.95,16.17 5.62,15.31 5.62,11.5C5.62,10.39 6,9.5 6.65,8.79C6.55,8.54 6.2,7.5 6.75,6.15C6.75,6.15 7.59,5.88 9.5,7.17C10.29,6.95 11.15,6.84 12,6.84C12.85,6.84 13.71,6.95 14.5,7.17C16.41,5.88 17.25,6.15 17.25,6.15C17.8,7.5 17.45,8.54 17.35,8.79C18,9.5 18.38,10.39 18.38,11.5C18.38,15.32 16.04,16.16 13.81,16.41C14.17,16.72 14.5,17.33 14.5,18.26C14.5,19.6 14.5,20.68 14.5,21C14.5,21.27 14.66,21.59 15.17,21.5C19.14,20.16 22,16.42 22,12A10,10 0 0,0 12,2Z" />
                        </svg>
                        <span>{item.text}</span>
                    </a>
                )}                
            </div>
        </section>
    );
}

export default SampleApps