import React, {useContext, useState} from 'react';
import Button from "../components/Button/Button";
import useInput from "../hooks/useInput";
import './LoginForm.css'
import {Context} from "../index";

const LoginForm = () => {
    const [isOpen, setOpen] = useState(true);

    const email = useInput('');
    const password = useInput('');

    const {store} = useContext(Context);

    return isOpen && (
        <div className="modal">
            <div className="modal-body">
                <input
                    type="text"
                    placeholder="Email"
                    {...email}
                />
                <input
                    type="text"
                    placeholder="Password"
                    {...password}
                />
                <Button
                    onClick={() => store.login(email, password)}
                >Sign in</Button>
            </div>
        </div>
    );
};

export default LoginForm;