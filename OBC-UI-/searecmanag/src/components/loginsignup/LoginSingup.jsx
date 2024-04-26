import React, { useState } from 'react';
import './LoginSingup.css';
import digi from '../assets/digi.jpg';
import email_icon from '../assets/email.png';
import password_icon from '../assets/password.png';
import user_icon from '../assets/user.png';

export const LoginSingup = () => {
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
                        <img src={user_icon} alt="" /> 
                        <input type="text" placeholder="Name" id="" /> 
                    </div>
                )}
                <div className="input">
                    <img src={email_icon} alt="" /> 
                    <input 
                        type="email" 
                        placeholder="Email" 
                        value={email} 
                        onChange={(e) => setEmail(e.target.value)} 
                    /> 
                </div>
                <div className="input">
                    <img src={password_icon} alt="" /> 
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
            <div className="submit-container">
                {!authenticated ? (
                    <div className={action === "Login" ? "submit gray" : "submit"} onClick={handleLogin}>
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
