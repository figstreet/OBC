import React, { useState } from 'react';
import './LoginSignup.css'; 
import digi from '../Assets/digi.jpg';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faEnvelope, faUser, faLock } from '@fortawesome/free-solid-svg-icons';

export const LoginSignup = () => {
    const [action, setAction] = useState("Login to your SeaRec Manager");
    const [showNameInput, setShowNameInput] = useState(false);
    const [showForgotPassword, setShowForgotPassword] = useState(true);
    const [showRegisterOption, setShowRegisterOption] = useState(true);
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [authenticated, setAuthenticated] = useState(false);
    const [loginButtonText, setLoginButtonText] = useState("Login"); 

    const handleLogin = async () => {
        try {
            const response = await fetch('/api/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ email, password })
            });

            if (response.ok) {
                // Authentication successful
                setAuthenticated(true);
                // Redirect other action upon successful login
            } else {
                // Authentication failed
                const errorData = await response.json();
                alert(errorData.message);
            }
        } catch (error) {
            console.error('Error:', error);
            alert('An error occurred while logging in. Please try again later.');
        }
    };

    const handleRegisterClick = () => {
        setShowNameInput(true);
        setAction("Register"); // Update the action to "Register"
        setShowForgotPassword(false);
        setShowRegisterOption(false);
        setLoginButtonText("Register"); // This updates the login button text to "Register" when register button is clicked
    };

    const handleCancelRegister = () => {
        setShowNameInput(false);
        setShowForgotPassword(true);
        setShowRegisterOption(true);
        setAction("Login to your SeaRec Manager"); // Reset action back to "Login to your SeaRec Manager"
        setLoginButtonText("Login"); // This Resets it back to "Login"
    };

    return (
        <div className='container'>
            <div className="cancel-register">
                {showNameInput && ( // this shows cancel button only if register form is visible
                    <button onClick={handleCancelRegister}>X</button>
                )}
            </div>
            <div className="header">
                <div className="text">{action}</div>
                <div className="underline"></div>
            </div>
            <div className="inputs">
                {showNameInput && (
                    <div className="input">
                        <FontAwesomeIcon icon={faUser} /> 
                        <input type="text" placeholder="Name" id="" /> 
                    </div>
                )}
                <div className="input">
                    <FontAwesomeIcon icon={faEnvelope} /> 
                    <input 
                        type="email" 
                        placeholder="Email" 
                        value={email} 
                        onChange={(e) => setEmail(e.target.value)} 
                    /> 
                </div>
                <div className="input">
                    <FontAwesomeIcon icon={faLock} /> 
                    <input 
                        type="password" 
                        placeholder="Password" 
                        value={password} 
                        onChange={(e) => setPassword(e.target.value)} 
                    /> 
                </div>
            </div>
            {showForgotPassword && (
                <div className="forgot-password">Forgot password? <span>Click Here?</span></div>
            )}
            <div className="login-container">
                {!authenticated ? (
                    <div className={action === "Login" ? "login gray" : "login"} onClick={handleLogin}>
                        {loginButtonText} {/* Display dynamic login/register button text */}
                    </div>
                ) : (
                    <div>Logged in!</div>
                )}
            </div>
            {showRegisterOption && (
                <p className="register-option">I haven't registered yet. <button id="register-button" onClick={handleRegisterClick}>Register</button></p>
            )}
        </div>
    );
};
