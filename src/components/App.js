import React, { Component } from 'react';
import Navbar from "./navbar/Navbar";
import Header from "./header/Header";
import Sections from './sections/Sections';
import Footer from './footer/Footer';
import './App.css';

import ReactGA from 'react-ga';
ReactGA.initialize('UA-50551684-7');
ReactGA.pageview(window.location.pathname + window.location.search);

class App extends Component {
  render() {
    return (
      <div className="root">
        <Navbar />
        <Header />
        <Sections />
        <Footer />
      </div>
    );
  }
}

export default App;
