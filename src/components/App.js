import React, { Component } from 'react';
import Navbar from "./navbar/Navbar";
import Header from "./header/Header";
import Content from './content/Content';
import './App.css';

class App extends Component {
  render() {
    return (
      <div >
        <Navbar />
        <Header />
        <Content />
      </div>
    );
  }
}

export default App;
