import React from 'react';
import './AdStyle.css';

import AdImage from '../AdImage/AdImage';

const Ad = ({ad}) => {
    return (
        <div className="ad">
            <div className="ad__body">
                <AdImage id={ad.id}/>
                <h2 className="ad__title">{ad.title}</h2>
                <p className="ad__description">{ad.releaseYear}</p>
                <p className="ad__description">{ad.description}</p>
                <p className="price">{ad.price} $</p>
            </div>
            <button className="ad__btn">View Ad</button>
        </div>
    );
};

export default Ad;
