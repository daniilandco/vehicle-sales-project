import React, {useEffect, useRef} from 'react';
import './SearchBar.css';
import {useSearch} from '../../providers/SearchProvider';

const SearchBar = () => {
    const search = useSearch();
    const inputRef = useRef(null);

    const getCurrentValue = () => inputRef.current.value.trim();

    useEffect(() => {
        console.log('search bar render!');
    });

    return (
        <div className="search__bar">
            <input ref={inputRef} type="text" className="input__form" placeholder="ad search"/>
            <button className="search-button" onClick={() => search.onText(getCurrentValue())}>
                Search
            </button>
        </div>
    );
};

export default SearchBar;
