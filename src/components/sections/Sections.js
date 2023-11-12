import React from 'react';
import GhButtons from './gh-buttons/GhButtons';
import WhatIsIt from './whatIsIt/WhatIsIt';
import Differences from './differences/Differences';
import Users from './users/Users';
import Advertisment from './advertisment/Advertisment';
import Download from './download/Download';
import GettingStarted from './gettingStarted/GettingStarted';
import SampleApps from './sampleApps/SampleApps';
import "./Sections.css";

const Sections = () => {
    return (
        <div className="root-dim">
            <section className="section-dim"><Advertisment /></section>
            <section className="section-dim"><GhButtons /></section>
            <section className="section-dim"><WhatIsIt /></section>
            <section className="section-dim"><Differences /></section>
            <section className="section-dim"><Users /></section>
            <section className="section-dim"><Download /></section>
            <section className="section-dim"><GettingStarted /></section>
            <section className="last-section-dim"><SampleApps /></section>
        </div>
    );
}

export default Sections