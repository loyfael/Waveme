import React, { useState, useEffect, useRef } from 'react';
import { FaBars } from 'react-icons/fa';
import './HamburgerMenu.css';

const HamburgerMenu = ({ initialOpen = false }) => {
  const [isOpen, setIsOpen] = useState(initialOpen);
  const menuRef = useRef(null);
  const buttonRef = useRef(null);

  const toggleMenu = () => {
    setIsOpen((prevIsOpen) => !prevIsOpen);
  };

  const closeMenu = () => {
    setIsOpen(false);
  };

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (
        menuRef.current && !menuRef.current.contains(event.target) &&
        buttonRef.current && !buttonRef.current.contains(event.target)
      ) {
        closeMenu();
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, []);

  return (
    <div className="hamburger-menu">
      <div ref={buttonRef} className="menu-icon" onClick={toggleMenu}>
        <FaBars />
      </div>
      {isOpen && <div className="overlay" />}
      <nav ref={menuRef} className={`menu ${isOpen ? 'open' : ''}`}>
        <ul>
          <li><a href="#home" onClick={closeMenu}>Home</a></li>
          <li><a href="#about" onClick={closeMenu}>About</a></li>
          <li><a href="#services" onClick={closeMenu}>Soundspack</a></li>
          <li><a href="#contact" onClick={closeMenu}>Download</a></li>
        </ul>
      </nav>
    </div>
  );
};

export default HamburgerMenu;
