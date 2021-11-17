import React from 'react';
import ReactDOM from 'react-dom';
import App from './App';
import Store from "./store/store";

const store = new Store();

export const Context = React.createContext({
    store,
});

ReactDOM.render(
    <React.StrictMode>
        <Context.Provider value={{
            store
        }}>
            <App/>
        </Context.Provider>
    </React.StrictMode>,
    document.getElementById('app'),
);
