import React from 'react';
import {SearchProvider} from '../providers/SearchProvider';
import Footer from '../components/Footer/Footer';
import SearchBar from '../components/SearchBar/SearchBar';
import SearchList from '../components/SearchList/SearchList';
import NavBar from "../components/NavBar/NavBar";

const Main = () => {
    return (
        <div>
            <NavBar/>
            <SearchProvider>
                <SearchBar/>
                <SearchList/>
            </SearchProvider>
            <Footer/>
        </div>
    );
};

export default Main;
