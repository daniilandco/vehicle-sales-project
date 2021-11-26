import React, {useContext} from 'react';
import Button from "../components/Button/Button";
import useInput from "../hooks/useInput";
import {Context} from "../index";

const RegisterForm = () => {
    const {store} = useContext(Context);

    const firstName = useInput('');
    const secondName = useInput('');
    const email = useInput('');
    const phoneNumber = useInput('');
    const password = useInput('');

    return (
        <div className="login__form">
            <input
                className="input"
                type="text"
                placeholder="First name"
                {...firstName}
            />
            <input
                className="input"
                type="text"
                placeholder="Second name"
                {...secondName}
            />
            <input
                className="input"
                type="text"
                placeholder="Email"
                {...email}
            />
            <input
                className="input"
                type="text"
                placeholder="Phone number"
                {...phoneNumber}
            />
            <input
                className="input"
                type="text"
                placeholder="Password"
                {...password}
            />
            <Button
                onClick={() => {
                    store.register(
                        firstName.value,
                        secondName.value,
                        email.value,
                        phoneNumber.value,
                        password.value
                    );
                }}
            >Register</Button>
        </div>
    );
};

export default RegisterForm;