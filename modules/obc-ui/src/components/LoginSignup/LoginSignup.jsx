import React, { useState, useEffect } from 'react';
import './LoginSignup.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faEnvelope, faUser, faLock } from '@fortawesome/free-solid-svg-icons';

const LoginSignup = () => {
    // Variables to manage form data and validation
    const [showLoginForm, setShowLoginForm] = useState(true);
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [emailError, setEmailError] = useState("");
    const [passwordError, setPasswordError] = useState("");
    const [confirmPasswordError, setConfirmPasswordError] = useState("");
    const [emailUnique, setEmailUnique] = useState(true);
    const [formValid, setFormValid] = useState(false);
    const [authenticated, setAuthenticated] = useState(false);

    // The validate form whenever the specified dependencies change
    useEffect(() => {
        validateForm();
    }, [email, password, confirmPassword]);

    useEffect(() => {
        console.log('form is valid: '+formValid);
    },[formValid]);

    // This validate email format
    const validateEmail = (email) => {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(email);
    };

    //This validate password requirements
    const validatePassword = (password) => {
        const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*?&\/])[A-Za-z\d@$!%*?&\/]{8,}$/;
        return passwordRegex.test(password);
    };

    // This validate the entire form
    const validateForm = async () => {
        const isEmailValid = validateEmail(email);
        const isPasswordValid = validatePassword(password);
        const isConfirmPasswordValid = password === confirmPassword;

        setEmailError(isEmailValid ? "" : "Invalid email format");
        setPasswordError(isPasswordValid ? "" : "Password must be at least 8 characters long and include at least one letter and one number");
        setConfirmPasswordError(isConfirmPasswordValid ? "" : "Passwords do not match");

        if (isEmailValid) {
            try {
                const response = await fetch('/api/check-email', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ email })
                });
                console.log(JSON.stringify(response))

                const data = await response.json();
                setEmailUnique(data.isUnique);
                if (!data.isUnique) {
                    setEmailError("Email is already in use");
                }
            } catch (error) {
                console.error('Error:', error);
            }
        }

        // console.log("is email valid: "+isEmailValid);
        // console.log("is password valid: "+isPasswordValid);
        // console.log("is confirm pass valid: "+isConfirmPasswordValid);

        setFormValid(isEmailValid && isPasswordValid && isConfirmPasswordValid /*&& emailUnique*/);
    };
    // This handles email input change and check for email uniqueness
    const handleEmailChange = (e) => {
        const newEmail = e.target.value;
        setEmail(newEmail);
    };

    // The handle user login
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
                setAuthenticated(true);
                alert('Login successful');
            } else {
                const errorData = await response.json();
                alert(errorData.message);
            }
        } catch (error) {
            console.error('Error:', error);
            alert('An error occurred while logging in. Please try again later.');
        }
    };

    // This handles user registration
    const handleRegister = async (e) => {
        e.preventDefault();
        if (!formValid) return;

        try {
            const response = await fetch('/api/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ email, firstName, lastName, password })
            });

            if (response.ok) {
                alert('Registration successful. Please check your email to confirm your account.');
                setShowLoginForm(true);
                clearRegistrationForm();
            } else {
                const errorData = await response.json();
                alert(errorData.message);
            }
        } catch (error) {
            console.error('Error:', error);
            alert('An error occurred during registration. Please try again later.');
        }
    };

    // This clears the registration form
    const clearRegistrationForm = () => {
        setEmail("");
        setFirstName("");
        setLastName("");
        setPassword("");
        setConfirmPassword("");
        setEmailError("");
        setPasswordError("");
        setConfirmPasswordError("");
        setEmailUnique(true);
    };

    return (
        <div className='container'>
            {showLoginForm ? (
                <>
                    <div className="header">
                        <div className="text">Login to your SeaRec Manager</div>
                        <div className="underline"></div>
                    </div>
                    <div className="inputs">
                        <div className="input">
                            <FontAwesomeIcon icon={faEnvelope} />
                            <input
                                type="email"
                                placeholder="Email"
                                value={email}
                                onChange={(e) => {
                                    setEmail(e.target.value);
                                    validateForm();
                                }}
                            />
                            {emailError && <div className="error">{emailError}</div>}
                        </div>
                        <div className="input">
                            <FontAwesomeIcon icon={faLock} />
                            <input
                                type="password"
                                placeholder="Password"
                                value={password}
                                onChange={(e) => {
                                    setPassword(e.target.value);
                                    validateForm();
                                }}
                            />
                            {passwordError && <div className="error">{passwordError}</div>}
                        </div>
                    </div>
                    <div className="forgot-password">Forgot password? <span>Click Here?</span></div>
                    <div className="login-container">
                        {!authenticated ? (
                            <div className="login" onClick={handleLogin}>
                                Login
                            </div>
                        ) : (
                            <div>Logged in!</div>
                        )}
                    </div>
                    <p className="register-option">I don't have an account. <button onClick={() => setShowLoginForm(false)}>Register</button></p>
                </>
            ) : (
                <>
                    <div className="header">
                        <div className="text">Register for SeaRec Manager</div>
                        <div className="underline"></div>
                    </div>
                    <form onSubmit={handleRegister} className="inputs">
                        <div className="input">
                            <FontAwesomeIcon icon={faEnvelope} />
                            <input
                                type="email"
                                placeholder="Email"
                                value={email}
                                onChange={(e) => {
                                    handleEmailChange(e);
                                    validateForm();
                                }}
                                onBlur={validateForm}
                            />
                        </div>
                        <>
                            {(emailError != "" && formValid) && <div className="error">{emailError}</div>}
                        </>
                        <div className="input">
                            <FontAwesomeIcon icon={faUser} />
                            <input
                                type="text"
                                placeholder="First Name (optional)"
                                value={firstName}
                                onChange={(e) => setFirstName(e.target.value)}
                            />
                        </div>
                        <div className="input">
                            <FontAwesomeIcon icon={faUser} />
                            <input
                                type="text"
                                placeholder="Last Name"
                                value={lastName}
                                onChange={(e) => setLastName(e.target.value)}
                            />
                        </div>
                        <div className="input">
                            <FontAwesomeIcon icon={faLock} />
                            <input
                                type="password"
                                placeholder="Password"
                                value={password}
                                onChange={(e) => {
                                    setPassword(e.target.value);
                                    validateForm();
                                }}
                                onBlur={validateForm}
                            />
                            
                        </div>
                        <>
                            {passwordError && <div className="error">{passwordError}</div>}
                        </>
                        <div className="input">
                            <FontAwesomeIcon icon={faLock} />
                            <input
                                type="password"
                                placeholder="Confirm Password"
                                value={confirmPassword}
                                onChange={(e) => {
                                    setConfirmPassword(e.target.value);
                                    validateForm();
                                }}
                                onBlur={validateForm}
                            />
                        </div>
                        <>
                            {confirmPasswordError && <div className="error">{confirmPasswordError}</div>}
                        </>
                        <div className="signup-container">
                            <button type="submit" className={`login ${!formValid ? 'gray' : ''}`} disabled={!formValid}>
                                Register
                            </button>
                            <button type="button" className="cancel" onClick={clearRegistrationForm}>
                                Cancel
                            </button>
                        </div>
                    </form>
                    <p className="register-option">I already have an account. <button onClick={() => setShowLoginForm(true)}>Login</button></p>
                </>
            )}
        </div>
    );
};

export default LoginSignup;
