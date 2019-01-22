import React, { Component } from 'react';
import "./Header.css"

export default class Header extends Component {
    
    componentDidMount() {
        const canvas = this.canvas
        window.onresize = () => this.resizeCanvas(canvas)
        this.resizeCanvas(canvas)
    }

    resizeCanvas = (canvas) => {
        canvas.style.width = '100%';
        canvas.style.height= '100%';
        
        canvas.width  = canvas.offsetWidth;
        canvas.height = canvas.offsetHeight;

        this.draw(canvas)
    }

    draw = canvas => {
        const context = canvas.getContext("2d");
        context.fillStyle = '#ef5451';
        context.beginPath();
        context.arc(canvas.width/2, canvas.height/2, canvas.width, 0, 2 * Math.PI);
        context.fill()

        context.fillStyle = '#e57373';
        context.beginPath();
        context.arc(canvas.width/2, canvas.height/2, canvas.width/2.5, 0, 2 * Math.PI);
        context.fill()

        context.fillStyle = '#ef9a9b';
        context.beginPath();
        context.arc(canvas.width/2, canvas.height/2, canvas.width/4, 0, 2 * Math.PI);
        context.fill()
    }

    render () {
        return (
            <header className="header header-dim">

                <canvas ref={element => this.canvas = element} className="header-background header-dim" />

                <div className="header-conten">
                    <img 
                        className="logo-dim"
                        src="https://firebasestorage.googleapis.com/v0/b/androidyoutubeplayer-sampleapp.appspot.com/o/android-youtube-player-logo.webp?alt=media&token=12ad47c4-52a5-43c5-88eb-0fe39cc5078c"
                        alt="logo"
                    />
                    <div className="description description-dim">
                        <div>YouTube Player library for Android and Chromecast, stable and customizable.</div>
                    </div>
                </div>

            </header>
        );
    }
}