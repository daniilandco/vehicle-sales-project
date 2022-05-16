import React from 'react';
import {SearchProvider} from '../providers/SearchProvider';
import Footer from '../components/Footer/Footer';
import SearchBar from '../components/SearchBar/SearchBar';
import SearchList from '../components/SearchList/SearchList';
import NavBar from "../components/NavBar/NavBar";

const Main = () => {
    return (
        <div>
            <SearchProvider>
                <SearchBar/>
                <SearchList/>
            </SearchProvider>
            <Footer/>
        </div>
    );
};

export default Main;
