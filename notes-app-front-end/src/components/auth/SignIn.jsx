import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import AuthService from '../../services/AuthService';
import { ToastContainer, toast } from 'react-toastify';
import { GoogleLogin } from "@react-oauth/google"; // Google OAuth


const SignIn = ({ isSigned, signMeIn }) => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    // Handle Sign In form submission
    const handleSignIn = async (e) => {
        e.preventDefault();
        try {
            const resp = await AuthService.signin(username, password);
            // console.log(resp);

            if (resp.data.username != null) {
                toast("Welcome back " + username)
                location.reload()
                if (localStorage.getItem('role') === "ROLE_ADMIN" || localStorage.getItem('role') === "ROLE_SU") {
                    navigate("/admin/home");
                }
                else {
                    signMeIn
                    navigate("/user/all-notes")
                }
            } else if (resp.data.message != null) {
                toast("Bad Credentials " + username)
                setTimeout(() => {
                    navigate("/signin")
                    setError("BackEnd-Error came / Or user doesn't exists")
                }, 1000);
            }
        } catch (error) {
            setError(error.message)
        }
    };


    return (
        <div className="flex justify-center items-center h-screen bg-gray-100">
            <div className="w-full max-w-sm p-6 bg-white rounded-lg shadow-lg">
                <h2 className="text-2xl font-semibold text-center text-gray-800 mb-4">Sign In</h2>
                {/* Error Message */}
                {error && <div className="text-red-500 text-center mb-4">{error}</div>}

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

                <form onSubmit={handleSignIn}>
                    {/* Username Field */}
                    <div className="mb-4">
                        <label htmlFor="username" className="block text-sm font-medium text-gray-700">Username</label>
                        <input
                            type="text"
                            id="username"
                            name="username"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            className="w-full px-4 py-2 mt-1 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                            required
                        />
                    </div>

                    {/* Password Field */}
                    <div className="mb-6">
                        <label htmlFor="password" className="block text-sm font-medium text-gray-700">Password</label>
                        <input
                            type="password"
                            id="password"
                            name="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            className="w-full px-4 py-2 mt-1 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                            required
                        />
                    </div>

                    {/* Submit Button */}
                    <button
                        type="submit"
                        className="w-full py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600 transition"
                    >
                        Sign In
                    </button>
                </form>
                <div className="mt-4 text-center text-blue-500 hover:text-blue-700">
                    <Link to="/forgot-password"> Forgot Password</Link>
                </div>
                {/* Sign Up Link */}
                <div className="mt-4 text-center">
                    <span className="text-sm text-gray-600">
                        Don't have an account?{' '}
                        <a href="/signup" className="text-blue-500 hover:text-blue-700">Sign up</a>
                    </span>
                </div>
            </div>
        </div>
    );
};

export default SignIn;
