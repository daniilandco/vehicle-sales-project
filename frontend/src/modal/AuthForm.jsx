import React, {useState} from 'react';
import './LoginForm.css'
import LoginForm from "./LoginForm";
import './AuthForm.css';
import RegisterForm from "./RegisterForm";

const AuthForm = ({active}) => {
    const [isActive, setActive] = useState(active);
    const [toggleState, setToggleState] = useState(1);

    const toggleTab = (index) => {
        console.log(index);
        setToggleState(index);
    }

    return isActive && (
        <div className="modal" onClick={() => setActive(false)}>
            <div className="modal-body" onClick={e => e.stopPropagation()}>
                <div className="container">
                    <i className="far fa-times-circle" onClick={() => setActive(false)}/>

                    <div className="bloc-tabs">
                        <button
                            className={toggleState === 1 ? "tabs active-tabs" : "tabs"}
                            onClick={() => toggleTab(1)}
                        >
                            Sign in
                        </button>
                        <button
                            className={toggleState === 2 ? "tabs active-tabs" : "tabs"}
                            onClick={() => toggleTab(2)}
                        >
                            Register
                        </button>
                    </div>
                    <div className="content-tabs">
                        <div
                            className={toggleState === 1 ? "content  active-content" : "content"}
                        >
                            <LoginForm/>
                        </div>
                        <div
                            className={toggleState === 2 ? "content  active-content" : "content"}
                        >
                            <RegisterForm/>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    );
};

export default AuthForm;