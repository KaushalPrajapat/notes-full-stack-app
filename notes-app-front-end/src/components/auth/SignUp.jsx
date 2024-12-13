import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios'; // For making HTTP requests
import AuthService from '../../services/AuthService';

const SignUp = () => {
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [error, setError] = useState('');
    const [successMessage, setSuccessMessage] = useState('');
    const navigate = useNavigate();

    // Handle form submission
    const handleSubmit = async (e) => {
        e.preventDefault();

        // Validate if passwords match
        if (password !== confirmPassword) {
            setError('Passwords do not match.');
            return;
        }
        // Reset any previous error or success messages
        setError('');
        setSuccessMessage('');

        try {
            // Send signup request to backend service (update this URL to your actual backend endpoint)
            const response = await AuthService.signup(username, email, password, "GUEST");

            if (response.data.message != null) {
                setSuccessMessage(response.data.message);
                setTimeout(() => {
                    navigate('/signin'); // Redirect to sign-in page after 3 seconds
                }, 3000);
            }
        } catch (error) {
            // Handle any error that occurs during the signup process
            setError('Error during signup. Please try again later.');
        }
    };

    return (
        <div className="bg-gray-100">
            <div className="max-w-md mx-auto p-6 bg-white shadow-md rounded-lg ">
                <h2 className="text-xl font-semibold text-center mb-1">Create an Account</h2>

                {/* Error or Success Message */}
                {error && <p className="text-red-500 bg-red-100 text-center mb-4">{error}</p>}
                {successMessage && <p className="text-green-500 text-center mb-4">{successMessage}</p>}
                {/* Google Login Button */}
                <a
                    href='http://localhost:8080/oauth2/authorization/github'
                    className="flex justify-center px-6 py-3 mb-4 bg-blue-300 text-white font-semibold rounded-lg shadow-md hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-opacity-50 transition-all"
                >
                    <img
                        src="/images/github.png"
                        alt="Google Logo"
                        className="w-6 h-6 mr-3"
                    />
                    Login With Github
                </a>
                <a
                    href='http://localhost:8080/oauth2/authorization/google'
                    className="flex justify-center px-6 py-3 mb-4 bg-blue-300 text-white font-semibold rounded-lg shadow-md hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-opacity-50 transition-all"
                >
                    <img
                        src="/images/google.png"
                        alt="Google Logo"
                        className="w-6 h-6 mr-3"
                    />
                    Login With Google
                </a>
                <form onSubmit={handleSubmit}>
                    {/* Username Field */}
                    <div className="mb-2">
                        <label htmlFor="username" className="block text-lg font-medium text-gray-700">Username</label>
                        <input
                            type="text"
                            id="username"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            className="w-full p-3 mt-1 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                            required
                        />
                    </div>

                    {/* Email Field */}
                    <div className="mb-2">
                        <label htmlFor="email" className="block text-lg font-medium text-gray-700">Email</label>
                        <input
                            type="email"
                            id="email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            className="w-full p-3 mt-1 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                            required
                        />
                    </div>

                    {/* Password Field */}
                    <div className="mb-2">
                        <label htmlFor="password" className="block text-lg font-medium text-gray-700">Password</label>
                        <input
                            type="password"
                            id="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            className="w-full p-3 mt-1 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                            required
                        />
                    </div>

                    {/* Confirm Password Field */}
                    <div className="mb-2">
                        <label htmlFor="confirmPassword" className="block text-lg font-medium text-gray-700">Confirm Password</label>
                        <input
                            type="password"
                            id="confirmPassword"
                            value={confirmPassword}
                            onChange={(e) => setConfirmPassword(e.target.value)}
                            className="w-full p-3 mt-1 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                            required
                        />
                    </div>

                    {/* Submit Button */}
                    <div className="text-center">
                        <button
                            type="submit"
                            className="px-6 py-3 bg-blue-500 text-white font-semibold rounded-md hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-500"
                        >
                            Sign Up
                        </button>
                    </div>
                </form>

                {/* Link to Login page */}
                <div className="text-center mt-4">
                    <p className="text-gray-600">
                        Already have an account?{' '}
                        <a href="/signin" className="text-blue-500 hover:text-blue-700">
                            Log in
                        </a>
                    </p>
                </div>
            </div>
        </div>
    );
};

export default SignUp;
