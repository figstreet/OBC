import React, { useState, useRef, useEffect } from 'react';
import './LoginRegistra.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faEnvelope, faUser, faLock, faEye, faEyeSlash } from '@fortawesome/free-solid-svg-icons';

const LoginSignup = () => {
    const emailRef = useRef(null);
    const passwordRef = useRef(null);

    const [showLoginForm, setShowLoginForm] = useState(true);
    const [showResetForm, setShowResetForm] = useState(false);
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [showPassword, setShowPassword] = useState(false);
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [emailError, setEmailError] = useState("");
    const [passwordError, setPasswordError] = useState("");
    const [confirmPasswordError, setConfirmPasswordError] = useState("");
    const [formValid, setFormValid] = useState(false);
    const [authenticated, setAuthenticated] = useState(false);
    const [resetEmail, setResetEmail] = useState("");
    const [resetEmailError, setResetEmailError] = useState("");
    const [resetSuccessMessage, setResetSuccessMessage] = useState("");
    const [apiError, setApiError] = useState("");

    useEffect(() => {
        setApiError("");
    }, [showLoginForm, showResetForm]);

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
            setEmailError("");
        } else if (isEmailValid) {
            setEmailError("");
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
        setEmailError("");
    };

    const handlePasswordChange = (e) => {
        setPassword(e.target.value);
        setPasswordError("");
    };

    const handleEmailBlur = () => {
        validateForm();
        if (emailError === "" && passwordRef.current) {
            passwordRef.current.focus();
        }
    };

    const handleLogin = async () => {
        const isEmailValid = validateEmail(email);
        const isPasswordNotEmpty = password !== "";

        if (!isEmailValid) {
            setEmailError("Invalid email format");
            return;
        }

        if (!isPasswordNotEmpty) {
            setPasswordError("Password is required");
            return;
        }

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
                if (errorData.message === "Email does not exist") {
                    setEmailError("Email does not exist");
                } else {
                    setApiError(errorData.message);
                }
            }
        } catch (error) {
            console.error('Error:', error);
            setApiError('An error occurred while logging in. Please try again later.');
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
                setApiError(errorData.message);
            }
        } catch (error) {
            console.error('Error:', error);
            setApiError('An error occurred during registration. Please try again later.');
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
                setResetEmailError("");
            } else {
                const errorData = await response.json();
                setApiError(errorData.message);
            }
        } catch (error) {
            console.error('Error:', error);
            setApiError('An error occurred while requesting password reset. Please try again later.');
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
    };

    return (
        <div className='container'>
            {showResetForm ? (
                <>
                    <div className="header">
                        <div className="text">Reset Your Password</div>
                        <div className="underline"></div>
                    </div>
                    {apiError && <div className="error" style={{ color: 'red' }}>{apiError}</div>}
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
                            {apiError && <div className="error" style={{ color: 'red' }}>{apiError}</div>}
                            <div className="inputs">
                                <div className="input">
                                    <FontAwesomeIcon icon={faEnvelope} />
                                    <input
                                        type="email"
                                        placeholder="Email"
                                        value={email}
                                        onChange={handleEmailChange}
                                        onBlur={handleEmailBlur}
                                        ref={emailRef}
                                        maxLength={100}
                                        required
                                    />
                                    {emailError && <div className="error">{emailError}</div>}
                                </div>
                                <div className="input">
                                    <FontAwesomeIcon icon={faLock} />
                                    <input
                                        type={showPassword ? "text" : "password"}
                                        placeholder="Password"
                                        value={password}
                                        onChange={handlePasswordChange}
                                        ref={passwordRef}
                                        maxLength={100}
                                        required
                                    />
                                    <FontAwesomeIcon
                                        icon={showPassword ? faEye : faEyeSlash}
                                        onClick={() => setShowPassword(!showPassword)}
                                        className="password-toggle"
                                    />
                                    {passwordError && <div className="error">{passwordError}</div>}
                                </div>
                            </div>
                            <div className="forgot-password">
                                <span onClick={() => setShowResetForm(true)}> Forgot my Password</span>
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
                                <button onClick={() => setShowLoginForm(false)}>Register my Account</button>
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
                                        maxLength={100}
                                        required
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
                                        type={showPassword ? "text" : "password"}
                                        placeholder="Password"
                                        value={password}
                                        onChange={handlePasswordChange}
                                        onBlur={validateForm}
                                        maxLength={100}
                                        required
                                    />
                                    <FontAwesomeIcon
                                        icon={showPassword ?  faEye: faEyeSlash}
                                        onClick={() => setShowPassword(!showPassword)}
                                        className="password-toggle"
                                    />
                                </div>
                                {passwordError && <div className="error">{passwordError}</div>}
                                <div className="input" style={{ position: 'relative' }}>
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
                                        maxLength={100}
                                        required
                                    />
                                    {confirmPasswordError && (
                                        <div className="error" style={{ position: 'absolute', right: '-170%', top: '50%', transform: 'translateY(-50%)', whiteSpace: 'nowrap' }}>
                                            {confirmPasswordError}
                                        </div>
                                    )}
                                </div>
                                <div className="signup-container">
                                    <button type="submit" className={`login ${!formValid ? 'gray' : ''}`} disabled={!formValid}>
                                        Register
                                    </button>
                                    <button type="button" className="cancel" onClick={() => { clearRegistrationForm(); setShowLoginForm(true); }}>
                                        Cancel
                                    </button>
                                </div>
                            </form>
                        </>
                    )}
                </>
            )}
        </div>
    );
};

export default LoginSignup;
