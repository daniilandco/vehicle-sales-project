import React, {useContext, useReducer} from 'react';
import AdService from '../services/AdService';
import {init, reducer, UPDATE_ADS, UPDATE_LOADING} from '../reducers/SearchReducer';

const SearchContext = React.createContext();

export const useSearch = () => {
    return useContext(SearchContext);
};

export const SearchProvider = ({children}) => {
    const [state, dispatch] = useReducer(
        reducer,
        {
            ads: [],
            loading: false,
        },
        init,
    );

    const updateAds = async (terms) => {
        dispatch({
            type: UPDATE_LOADING,
            payload: true,
        });
        dispatch({
            type: UPDATE_ADS,
            payload: await AdService.fetchAds(terms),
        });
    };

    return (
        <SearchContext.Provider
            value={{
                ads: state.ads,
                loading: state.loading,
                onSearch: updateAds,
            }}
        >
            {children}
        </SearchContext.Provider>
    );
};
