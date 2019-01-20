import React from 'react';
import "./Navbar.css"

const Navbar = () => {
    return (
        <nav className="navbar navbar-dim">
            <span className="navbar-item navbar-item-dim">android-youtube-player</span>
            <div className="fill-space"></div>
            <span className="navbar-item navbar-item-dim">Documentation</span>
            <span className="navbar-item navbar-item-dim">Support</span>
            <span className="navbar-item navbar-item-dim">Open on GitHub</span>
        </nav>
    );
}

export default Navbar