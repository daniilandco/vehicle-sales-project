import React, {useEffect, useMemo, useRef} from 'react';
import './SearchBar.css';
import {useSearch} from '../../providers/SearchProvider';
import debounce from "lodash.debounce";

const SearchBar = () => {
    const search = useSearch();
    const inputRef = useRef(null);
    const previousValue = useRef('');

    const debouncedResults = useMemo(() => debounce(search.onSearch, 500), []);

    const getCurrentValue = () => inputRef.current.value.trim();

    useEffect(() => {
        previousValue.current = getCurrentValue();
        return () => debouncedResults.cancel();
    });

    return (
        <div className="search__bar">
            <input ref={inputRef}
                   type="text"
                   className="input__form"
                   placeholder="ad search"
                   onChange={() => {
                       if (previousValue.current !== getCurrentValue()) {
                           debouncedResults(getCurrentValue());
                       }
                   }}/>
            <button className="search-button">
                <i className="fa fa-search"/>
            </button>
        </div>
    );
};

export default SearchBar;
