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
        const navBarItemClass = this.state.currentScroll === 0 ? "" : "navbar-item-elevated";

        return (
            <nav className={"navbar navbar-dim " +navBarClass}>
                <button className={"navbar-item navbar-item-dim " +navBarItemClass}>Docs</button>
                <button className={"navbar-item navbar-item-dim " +navBarItemClass}>Support</button>
                <button className={"navbar-item navbar-item-dim " +navBarItemClass}>GitHub</button>
            </nav>
        );
    }
}

export default Navbar