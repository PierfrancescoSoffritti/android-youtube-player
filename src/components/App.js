import React, { Component } from 'react';
import Navbar from "./navbar/Navbar";
import Header from "./header/Header";
import './App.css';

class App extends Component {
  render() {
    return (
      <div >
        <Navbar />
        <Header />
      </div>
    );
  }
}

export default App;
