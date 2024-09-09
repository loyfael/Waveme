import React from 'react';
import './FeaturePage.css';
import { FaVectorSquare, FaLockOpen, FaUsers } from 'react-icons/fa';

const FeaturesPage = () => {
    return (
        <>
            <div className="features-container">
                <div className="features-grid">
                    <div className="feature">
                        <FaVectorSquare className="feature-icon" />
                        <h3>Simple to use</h3>
                        <p>In our labs, we designed THE easiest-to-use soundboard.</p>
                    </div>
                    <div className="feature">
                        <FaLockOpen className="feature-icon" />
                        <h3>Open source</h3>
                        <p>Tired of paid soundboards? Use SoundBward. Free and open source software.</p>
                    </div>
                    <div className="feature">
                        <FaUsers className="feature-icon" />
                        <h3>Community</h3>
                        <p>Share your soundpack with the community, and get feedback.</p>
                    </div>
                </div>
            </div>
        </>
    );
};

export default FeaturesPage;
