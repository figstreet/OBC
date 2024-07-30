import React, { useState, useEffect, useRef } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faLock } from '@fortawesome/free-solid-svg-icons';
import './loginRegistra.css'; // Ensure this CSS file contains the relevant styles

const ResetPassword = ({ match }) => {
    const [newPassword, setNewPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [newPasswordError, setNewPasswordError] = useState('');
    const [confirmPasswordError, setConfirmPasswordError] = useState('');
    const [successMessage, setSuccessMessage] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const [formValid, setFormValid] = useState(false);
    const newPasswordRef = useRef(null);
    const confirmPasswordRef = useRef(null);
    // Validate password format
    const validatePassword = (password) => {
        const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*?&/]).{10,25}$/;
        return passwordRegex.test(password);
    };

    // Validate form inputs
    const validateForm = () => {
        const isNewPasswordValid = validatePassword(newPassword);
        const isConfirmPasswordValid = newPassword === confirmPassword;

        setNewPasswordError(isNewPasswordValid ? '' : 'Password must be 10-25 characters long, and include at least one letter, one number, and one special character.');
        setConfirmPasswordError(isConfirmPasswordValid ? '' : 'Passwords do not match');

        setFormValid(isNewPasswordValid && isConfirmPasswordValid);
    };

    useEffect(() => {
        validateForm();
    }, [newPassword, confirmPassword]);

    // Handle form submission
    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!formValid) return;

        try {
            const response = await fetch('/api/reset-password-confirm', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    token: match.params.token, // Get token from URL params
                    newPassword
                })
            });

            if (response.ok) {
                setSuccessMessage('Password successfully reset. You can now log in.');
                setNewPassword('');
                setConfirmPassword('');
            } else {
                const errorData = await response.json();
                setErrorMessage(errorData.message);
            }
        } catch (error) {
            console.error('Error:', error);
            setErrorMessage('An error occurred. Please try again later.');
        }
    };

    return (
        <div className='container'>
            <div className="header">
                <div className="text">Reset Your Password</div>
                <div className="underline"></div>
            </div>
            <form onSubmit={handleSubmit} className="inputs">
                <div className="input">
                    <FontAwesomeIcon icon={faLock} />
                    <input
                        type="password"
                        placeholder="New Password"
                        value={newPassword}
                        onChange={(e) => {
                            setNewPassword(e.target.value);
                            validateForm();
                        }}
                        ref={newPasswordRef} // Focus this input after email validation
                        minLength={10} // Min length for password
                        maxLength={25} // Max length for password
                        required // Mark field as required
                    />
                    {newPasswordError && <div className="error">{newPasswordError}</div>}
                </div>
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
                        ref={confirmPasswordRef} // Focus this input after email validation
                        minLength={10} // Min length for password is defined here
                        maxLength={25} // Max length for password is defined here
                        required // Mark field as required is defined here
                    />
                    {confirmPasswordError && <div className="error">{confirmPasswordError}</div>}
                </div>
                {successMessage && <div className="success">{successMessage}</div>}
                {errorMessage && <div className="error">{errorMessage}</div>}
                <div className="reset-container">
                    <button type="submit" className={`submit ${!formValid ? 'gray' : ''}`} disabled={!formValid}>
                        Reset Password
                    </button>
                </div>
            </form>
        </div>
    );
};

export default ResetPassword;