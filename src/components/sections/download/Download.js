import React from 'react';
import SyntaxHighlighter from 'react-syntax-highlighter';
import { agate  } from 'react-syntax-highlighter/dist/styles/hljs';
import "./Download.css";

const Download = () => {
    return (
        <section>
            <div className="section-title">Download</div>
            <div>
The Gradle dependency is available via <a href="https://jitpack.io/#PierfrancescoSoffritti/android-youtube-player" target="_blank" rel="noopener noreferrer">JitPack</a>.
<br/>The minimum API level supported by this library is API 17. Also, starting from version 9 your app needs to be using the androidx libraries instead of the old support libraries.

<br/><br/>

The <b>core</b> module is all you need to start playing YouTube videos in your app.

Add this to your root level build.gradle file.

<SyntaxHighlighter language='groovy' style={ agate }>{
`allprojects {
    repositories {
      // ...
      maven { url 'https://jitpack.io' }
    }
}`
}</SyntaxHighlighter>

Then add this to your module level build.gralde file.

<SyntaxHighlighter language='groovy' style={ agate }>{
`dependencies {
    implementation 'com.pierfrancescosoffritti.androidyoutubeplayer:core:{latest-version}'
}`
}</SyntaxHighlighter>

The <b>chromecast-sender</b> module is an optional extension library. Use it only if you need to cast YouTube videos from your app to a Chromecast device.

<SyntaxHighlighter language='groovy' style={ agate }>{
`dependencies {
    implementation 'com.pierfrancescosoffritti.androidyoutubeplayer:chromecast-sender:{latest-version}'
}`
}</SyntaxHighlighter>

            </div>
        </section>
    );
}

export default Download