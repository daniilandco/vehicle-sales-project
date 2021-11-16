import React, {useState} from 'react';
import './NavBar.css';
import {MenuItems} from "./MenuItems";
import Button from "../Button/Button";

const NavBar = () => {

    const [clicked, setClicked] = useState(false);

    const handleClick = () => {
        setClicked(prev => !prev);
    }

    return (
        <nav className="navbar">
            <h1 className="navbar-logo">D&C<i className="fas fa-car"/></h1>
            <div className="menu-icon" onClick={handleClick}>
                <i className={clicked ? 'fas fa-times' : 'fas fa-bars'}/>
            </div>
            <ul className={clicked ? 'nav-menu active' : 'nav-menu'}>
                {MenuItems.map((item, index) => {
                    return (
                        <li key={index}>
                            <a className={item.cName} href={item.url}>
                                {item.title}
                            </a>
                        </li>
                    );
                })}
            </ul>
            <Button>Sign in</Button>
        </nav>
    );
};
export default NavBar;
