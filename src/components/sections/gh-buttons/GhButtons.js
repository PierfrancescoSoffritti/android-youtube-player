import React from 'react';
import "./GhButtons.css"

const GhButtons = () => {
    return (
        <section>
            <a className="github-button" href="https://github.com/PierfrancescoSoffritti/android-youtube-player" data-icon="octicon-star" data-size="large" data-show-count="true" aria-label="Star PierfrancescoSoffritti/android-youtube-player on GitHub">Star</a>
            &nbsp;&nbsp;<a className="github-button" href="https://github.com/PierfrancescoSoffritti/android-youtube-player/fork" data-icon="octicon-repo-forked" data-size="large" data-show-count="true" aria-label="Fork PierfrancescoSoffritti/android-youtube-player on GitHub">Fork</a>
            &nbsp;&nbsp;<a className="github-button" href="https://github.com/PierfrancescoSoffritti" data-size="large" data-show-count="true" aria-label="Follow @PierfrancescoSoffritti on GitHub">Follow @PierfrancescoSoffritti</a>
        </section>
    );
}

export default GhButtons