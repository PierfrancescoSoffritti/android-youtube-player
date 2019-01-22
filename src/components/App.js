import React, { Component } from 'react';
import Navbar from "./navbar/Navbar";
import Header from "./header/Header";
import Sections from './sections/Sections';
import Footer from './footer/Footer';
import './App.css';

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
