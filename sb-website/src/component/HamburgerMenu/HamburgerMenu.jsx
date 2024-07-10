import React, { useState, useEffect, useRef } from 'react';
import { FaBars } from 'react-icons/fa';
import './HamburgerMenu.css';

const HamburgerMenu = ({ initialOpen = false }) => {
    const [isOpen, setIsOpen] = useState(initialOpen);
    const [hasStarted, setHasStarted] = useState(false);
    const menuRef = useRef(null);
    const buttonRef = useRef(null);
    const overlayRef = useRef(null);

    const toggleMenu = () => {
        setIsOpen((prevIsOpen) => !prevIsOpen);
        setHasStarted(true);
    };

    const closeMenu = () => {
        setIsOpen(false);
        setHasStarted(true);
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

    useEffect(() => {
        if (hasStarted) {
            if (!isOpen) {
                overlayRef.current.classList.add('close');
                overlayRef.current.classList.remove('open');
            } else {
                overlayRef.current.classList.add('open');
                overlayRef.current.classList.remove('close');
            }
        }
    }, [isOpen, hasStarted]);

    return (
        <div className="hamburger-menu">
            <div ref={buttonRef} className="menu-icon" onClick={toggleMenu}>
                <FaBars />
            </div>
            {(isOpen || hasStarted) && (
                <div ref={overlayRef} className={`overlay ${isOpen ? 'open' : 'close'}`} />
            )}
            <nav ref={menuRef} className={`menu ${isOpen ? 'open' : 'close'}`}>
                <ul>
                    <li><a href="#home" onClick={closeMenu}>Home</a></li>
                    <li><a href="#about" onClick={closeMenu}>About</a></li>
                    <li><a href="#services" onClick={closeMenu}>Soundspack</a></li>
                    <li><a href="#contact" onClick={closeMenu}>Download</a></li>
                    <hr />
                    <li><a href="#contact" onClick={closeMenu}>Sign up</a></li>
                </ul>
            </nav>
        </div>
    );
};

export default HamburgerMenu;
