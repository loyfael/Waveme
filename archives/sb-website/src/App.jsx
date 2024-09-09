import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import FeaturesPage from './page/Features/FeaturePage';
import HomePage from './page/Home/HomePage';
import HamburgerMenu from './component/HamburgerMenu/HamburgerMenu'; // Assurez-vous que le chemin est correct

function App() {
    return (
        <Router>
            <HamburgerMenu />
            <Routes>
                <Route path="/features" element={<FeaturesPage />} />
                <Route path="/" element={<HomePage />} />
                {/* Ajoutez d'autres routes ici */}
            </Routes>
        </Router>
    );
}

export default App;
