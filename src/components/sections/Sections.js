import React from 'react';
import Overview from "./overview/Overview";
import Download from './download/Download';
import GettingStarted from './gettingStarted/GettingStarted';
import "./Sections.css"

const Sections = () => {
    return (
        <div className="root-dim">
            <section className="section-dim"><Overview /></section>
            <section className="section-dim"><Download /></section>
            <section className="last-section-dim"><GettingStarted /></section>
        </div>
    );
}

export default Sections