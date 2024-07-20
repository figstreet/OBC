import React, { useState, useRef } from 'react';
import './loginRegistra.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faEnvelope, faUser, faLock } from '@fortawesome/free-solid-svg-icons';

const LoginSignup = () => {
    const emailRef = useRef(null);
    const passwordRef = useRef(null);

    const [showLoginForm, setShowLoginForm] = useState(true);
    const [showResetForm, setShowResetForm] = useState(false);
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
    const [resetEmail, setResetEmail] = useState("");
    const [resetEmailError, setResetEmailError] = useState("");
    const [resetSuccessMessage, setResetSuccessMessage] = useState("");

    const validateEmail = (email) => {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(email);
    };

    const validatePassword = (password) => {
        const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*?&\/])[A-Za-z\d@$!%*?&\/]{8,}$/;
        return passwordRegex.test(password);
    };

    const validateForm = () => {
        const isEmailValid = validateEmail(email);
        const isPasswordValid = validatePassword(password);
        const isConfirmPasswordValid = password === confirmPassword;

        if (email === "") {
            setEmailError(""); // This Clears the email error if email field is empty
        } else if (isEmailValid) {
            setEmailError(""); // This Clears theClear email error if email is valid
        } else {
            setEmailError("Invalid email format");
        }

        setConfirmPasswordError(isConfirmPasswordValid ? "" : "Passwords do not match");
        
        // console.log("is email valid: "+isEmailValid);
        // console.log("is password valid: "+isPasswordValid);
        // console.log("is confirm pass valid: "+isConfirmPasswordValid);

        setFormValid(isEmailValid && isPasswordValid && isConfirmPasswordValid);
    };

    const handleEmailChange = (e) => {
        setEmail(e.target.value);
        setEmailError(""); // This Clears the error when user starts typing
    };

    const handlePasswordChange = (e) => {
        setPassword(e.target.value);
    };

    const handleEmailBlur = async () => {
        validateForm();
        if (emailError === "" && passwordRef.current) {
            passwordRef.current.focus(); // This moves the focus to password field if email is valid
        }
    };

    const handleLogin = async () => {
        try {
            const isEmailValid = validateEmail(email);
            if (!isEmailValid) {
                setEmailError("Invalid email format");
                return;
            }

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
                if (errorData.message === "Email does not exist") {
                    setEmailError("Email does not exist");
                } else {
                    alert(errorData.message);
                }
            }
        } catch (error) {
            console.error('Error:', error);
            alert('An error occurred while logging in. Please try again later.');
        }
    };

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

    const handlePasswordResetRequest = async () => {
        if (!validateEmail(resetEmail)) {
            setResetEmailError("Invalid email format");
            return;
        }

        try {
            const response = await fetch('/api/request-password-reset', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ email: resetEmail })
            });

            if (response.ok) {
                setResetSuccessMessage('Please check your email for a password reset link. The link will be valid for 30 minutes.');
                setResetEmail("");
                setResetEmailError(""); // his clears the reset form fields and error
            } else {
                const errorData = await response.json();
                setResetEmailError(errorData.message);
            }
        } catch (error) {
            console.error('Error:', error);
            setResetEmailError('An error occurred while requesting password reset. Please try again later.');
        }
    };

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
            {showResetForm ? (
                <>
                    <div className="header">
                        <div className="text">Reset Your Password</div>
                        <div className="underline"></div>
                    </div>
                    <div className="inputs">
                        <div className="input">
                            <FontAwesomeIcon icon={faEnvelope} />
                            <input
                                type="email"
                                placeholder="Enter your registered email"
                                value={resetEmail}
                                onChange={(e) => setResetEmail(e.target.value)}
                                onBlur={() => {
                                    if (!validateEmail(resetEmail)) {
                                        setResetEmailError("Invalid email format");
                                    } else {
                                        setResetEmailError("");
                                    }
                                }}
                            />
                            {resetEmailError && <div className="error">{resetEmailError}</div>}
                        </div>
                        <div className="reset-container">
                            <button 
                                type="button" 
                                className={`submit ${!validateEmail(resetEmail) ? 'gray' : ''}`} 
                                disabled={!validateEmail(resetEmail)}
                                onClick={handlePasswordResetRequest}
                            >
                                 Reset
                            </button>
                        </div>
                        {resetSuccessMessage && <div className="Check your email we have sent you a link to reset your passaword">{resetSuccessMessage}</div>}
                    </div>
                    <p className="register-option">
                        <button onClick={() => setShowResetForm(false)}>Back to Login</button>
                    </p>
                </>
            ) : (
                <>
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
                                        onChange={handleEmailChange}
                                        onBlur={handleEmailBlur} // Move focus to password after validation
                                        ref={emailRef} // Sets initial focus
                                        maxLength={100} // Max length for email
                                        required // Marks field requirement
                                    />
                                    {emailError && <div className="error">{emailError}</div>}
                                </div>
                                <div className="input">
                                    <FontAwesomeIcon icon={faLock} />
                                    <input
                                        type="password"
                                        placeholder="Password"
                                        value={password}
                                        onChange={handlePasswordChange}
                                        ref={passwordRef} // Focus this input after email validation
                                        maxLength={100} // Max length for password is defined here
                                        required // Mark field as required is defined here
                                    />
                                    {passwordError && <div className="error">{passwordError}</div>}
                                </div>
                            </div>
                            <div className="forgot-password">
                                Forgot password? <span onClick={() => setShowResetForm(true)}>Click Here</span>
                            </div>
                            <div className="login-container">
                                {!authenticated ? (
                                    <div className="login" onClick={handleLogin}>
                                        Login
                                    </div>
                                ) : (
                                    <div>Logged in!</div>
                                )}
                            </div>
                            <p className="register-option">
                                I don't have an account. <button onClick={() => setShowLoginForm(false)}>Register</button>
                            </p>
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
                                        maxLength={100} // Max length for email
                                        required // Mark field as required
                                    />
                                </div>
                                {(emailError !== "" && formValid) && <div className="error">{emailError}</div>}
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
                                        onChange={handlePasswordChange}
                                        onBlur={validateForm}
                                        maxLength={100} // Max length for password
                                        required // Mark field as required
                                    />
                                </div>
                                {passwordError && <div className="error">{passwordError}</div>}
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
                                        maxLength={100} // Max length for password
                                        required // Mark field as required
                                    />
                                </div>
                                {confirmPasswordError && <div className="error">{confirmPasswordError}</div>}
                                <div className="signup-container">
                                    <button type="submit" className={`login ${!formValid ? 'gray' : ''}`} disabled={!formValid}>
                                        Register
                                    </button>
                                    <button type="button" className="cancel" onClick={clearRegistrationForm}>
                                        Cancel
                                    </button>
                                </div>
                            </form>
                            <p className="register-option">
                                I already have an account. <button onClick={() => setShowLoginForm(true)}>Login</button>
                            </p>
                        </>
                    )}
                </>
            )}
        </div>
    );
};

export default LoginSignup;
