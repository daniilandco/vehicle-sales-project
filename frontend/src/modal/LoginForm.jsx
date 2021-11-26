import React, {useContext} from 'react';
import Button from "../components/Button/Button";
import useInput from "../hooks/useInput";
import './LoginForm.css'
import {Context} from "../index";

const AuthForm = () => {
    const email = useInput('');
    const password = useInput('');

    const {store} = useContext(Context);

    return (
        <div className="login__form">
            <input
                className="input"
                type="text"
                placeholder="Email"
                {...email}
            />
            <input
                className="input"
                type="text"
                placeholder="Password"
                {...password}
            />
            <Button
                onClick={() => {
                    store.login(email.value, password.value)
                }}
            >Sign in</Button>
            </div>
    );
};

export default AuthForm;