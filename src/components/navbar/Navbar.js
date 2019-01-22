import React from 'react';
import "./Navbar.css"

const Navbar = () => {
    return (
        <nav className="navbar navbar-dim">
            {/* <button className="navbar-item navbar-item-dim navbar-item-not-important">android-youtube-player</button>
            <div className="fill-space navbar-item-not-important"></div> */}
            <button className="navbar-item navbar-item-dim">Docs</button>
            <button className="navbar-item navbar-item-dim">Support</button>
            <button className="navbar-item navbar-item-dim">GitHub</button>
        </nav>
    );
}

export default Navbar