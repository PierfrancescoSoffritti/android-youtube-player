import React, { Component } from 'react';
import "./Header.css"

export default class Header extends Component {

    mousePosition = { x: 0, y: 0 }
    currentOffset = { x: 0, y: 0 }

    componentDidMount() {
        const canvas = this.canvas
        window.onresize = () => this.resizeCanvas(canvas)
        window.onmousemove = this.onMouseMove
        this.resizeCanvas(canvas)
        this.updateCanvas()
    }

    onMouseMove = ({screenX, screenY}) => {
        this.mousePosition.x = screenX-this.canvas.width/2
        this.mousePosition.y = screenY-this.canvas.height/2
    }

    resizeCanvas = (canvas) => {
        canvas.style.width = '100%';
        canvas.style.height= '100%';
        
        canvas.width  = canvas.offsetWidth;
        canvas.height = canvas.offsetHeight;
    }

    draw = (canvas, time) => {
        const offsetSlow = Math.sin(time/820)*10
        const offsetFast = Math.sin(time/1200)*15

        this.currentOffset.x += (  (this.mousePosition.x * 0.06) - this.currentOffset.x ) * 0.01;
        this.currentOffset.y += ( -(this.mousePosition.y * 0.06) - this.currentOffset.y ) * 0.01;

        const context = canvas.getContext("2d");
        context.fillStyle = '#ef5451';
        context.beginPath();
        context.arc(canvas.width/2, canvas.height/2, canvas.width+offsetSlow, 0, 2 * Math.PI);
        context.fill()

        context.fillStyle = '#e57373';
        context.beginPath();
        context.arc(canvas.width/2 +this.currentOffset.x*0.5, canvas.height/2 +this.currentOffset.y*0.5, canvas.width/2.5 +offsetSlow, 0, 2 * Math.PI);
        context.fill()

        context.fillStyle = '#ef9a9b';
        context.beginPath();
        context.arc(canvas.width/2 + this.currentOffset.x, canvas.height/2 +this.currentOffset.y, canvas.width/4 +offsetFast, 0, 2 * Math.PI);
        context.fill()
    }

    updateCanvas = time => {
        requestAnimationFrame(this.updateCanvas);
        this.draw(this.canvas, time);
    }

    render () {
        return (
            <header className="header header-dim">

                <canvas ref={element => this.canvas = element} className="header-background header-dim" />

                <div className="header-conten header-conten-dim">
                    <img 
                        className="logo-dim"
                        src="https://firebasestorage.googleapis.com/v0/b/androidyoutubeplayer-sampleapp.appspot.com/o/Android-YouTube-Player_300px.webp?alt=media&token=d768f08c-58a1-4048-b77a-040ce57944da"
                        alt="logo"
                    />
                    
                    <div className="description description-dim">YouTube Player library for Android and Chromecast, stable and customizable.</div>
                </div>

            </header>
        );
    }
}