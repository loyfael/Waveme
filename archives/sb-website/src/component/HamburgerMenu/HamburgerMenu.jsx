import React, { useState, useEffect, useRef } from 'react';
import { FaBars } from 'react-icons/fa';
import { Link } from 'react-router-dom';
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
            <div ref={buttonRef} onClick={toggleMenu}>
                <FaBars className="menu-icon"/>
            </div>
            {(isOpen || hasStarted) && (
                <div ref={overlayRef} className={`overlay ${isOpen ? 'open' : 'close'}`} />
            )}
            <nav ref={menuRef} className={`menu ${isOpen ? 'open' : 'close'}`}>
                <ul>
                    <li><Link to="/" onClick={closeMenu}>Home</Link></li>
                    <li><Link to="/about" onClick={closeMenu}>About</Link></li>
                    <li><Link to="/services" onClick={closeMenu}>Soundspack</Link></li>
                    <li><Link to="/download" onClick={closeMenu}>Download</Link></li>
                    <hr />
                    <li><Link to="/signup" onClick={closeMenu}>Sign up</Link></li>
                </ul>
            </nav>
        </div>
    );
};

export default HamburgerMenu;
