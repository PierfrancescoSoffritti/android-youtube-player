import React, { Component } from 'react';
import "./Navbar.css"

function getCurrentScroll() { return (window.pageYOffset !== undefined) ? window.pageYOffset : (document.documentElement || document.body.parentNode || document.body).scrollTop }

class Navbar extends Component {
    constructor(props) {
        super(props);

        this.state = { currentScroll: 0 }
    }

    componentDidMount = () => {
        window.addEventListener('scroll', this.handleScroll);
    }
    
    componentWillUnmount = () => {
        window.removeEventListener('scroll', this.handleScroll);
    }
    
    handleScroll = event => {
        const currentScroll = getCurrentScroll()
        this.setState({ currentScroll });
    }

    render = () => {
        
        const navBarClass = this.state.currentScroll === 0 ? "" : "navbar-elevated";
        const navBarItemClass = this.state.currentScroll === 0 ? "" : "navbar-button-elevated";

        const navBarItems = [
            {src: "https://github.com/PierfrancescoSoffritti/android-youtube-player#table-of-contents-core", name: "Docs"},
            {src: "https://github.com/PierfrancescoSoffritti/android-youtube-player/issues", name: "Support"},
            {src: "https://github.com/PierfrancescoSoffritti/android-youtube-player", name: "GitHub"},
            
        ]

        return (
            <nav className={"navbar navbar-dim " +navBarClass}>
                {navBarItems.map(item => 
                    <a className="navbar-link navbar-item-dim" href={item.src} target="_blank" rel="noopener noreferrer">
                        <button className={"navbar-button navbar-item-dim " +navBarItemClass}>{item.name}</button>
                    </a>
                )}
            </nav>
        );
    }
}

export default Navbar