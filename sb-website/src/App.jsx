import React from 'react';
import HamburgerMenu from './component/HamburgerMenu/HamburgerMenu';
import Logo from "./component/Logo/Logo"
import './App.css';

function App() {
  return (
    <>
      <HamburgerMenu initialOpen={false} />
      <Logo />
    </>
  );
}

export default App;
