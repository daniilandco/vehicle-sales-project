import React, {useContext, useReducer} from 'react';
import DataService from '../services/DataService';
import {init, reducer, UPDATE_ADS, UPDATE_TERMS} from '../reducers/SearchReducer';

const SearchContext = React.createContext();

export const useSearch = () => {
    return useContext(SearchContext);
};

export const SearchProvider = ({children}) => {
    const [state, dispatch] = useReducer(
        reducer,
        {
            terms: '',
            ads: [],
        },
        init,
    );

    const updateTerms = (terms) => dispatch({type: UPDATE_TERMS, payload: terms});
    const updateAds = async () =>
        dispatch({
            type: UPDATE_ADS,
            payload: await DataService.getAds(state.terms),
        });

    return (
        <SearchContext.Provider
            value={{
                ads: state.ads,
                terms: state.terms,
                onText: updateTerms,
                onSearch: updateAds,
            }}
        >
            {children}
        </SearchContext.Provider>
    );
};
