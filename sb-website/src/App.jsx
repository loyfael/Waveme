import React from 'react';
import HamburgerMenu from './component/HamburgerMenu/HamburgerMenu';
import Banner from "./component/Banner/Banner"
import './App.css';

function App() {
  return (
    <>
      <HamburgerMenu initialOpen={false} />
      <Banner />
    </>
  );
}

export default App;
