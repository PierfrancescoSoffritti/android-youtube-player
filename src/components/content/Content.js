import React from 'react';
import Overview from "./overview/Overview";
import Download from './download/Download';
import GettingStarted from './gettingStarted/GettingStarted';
import "./Content.css"

const Content = () => {
    return (
        <section className="content-dim">
            <div className="section-dim"><Overview /></div>
            <div className="section-dim"><Download /></div>
            <div className="last-section-dim"><GettingStarted /></div>
        </section>
    );
}

export default Content