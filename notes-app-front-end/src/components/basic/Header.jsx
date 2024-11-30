import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import NoteLogo from './NoteLogo';

const Header = ({ isSigned, handleSignOut, userRole }) => {
    const [isMenuOpen, setIsMenuOpen] = useState(false);

    const toggleMenu = () => {
        setIsMenuOpen(!isMenuOpen);
    };

    return (
        <header className="flex justify-between items-center p-4 bg-gray-100 border-b border-gray-300">
            {/* Left section: Logo and buttons */}
            <div className="flex items-center space-x-4">
                {!isSigned &&
                    <NoteLogo linkTo={'/'} />
                }

                {/* Show Add Note and Show All if logged in */}
                {(isSigned && userRole === "ROLE_USER" || userRole === "ROLE_GUEST") && (
                    <>
                        <NoteLogo linkTo={'/user/all-notes'} />
                        <div className="hidden md:flex space-x-4">
                            <Link
                                to="/user/add-note"
                                className="px-4 py-2 text-white bg-blue-500 rounded hover:bg-blue-600 transition"
                            >
                                Add Note
                            </Link>
                            <Link
                                to="/user/all-notes"
                                className="px-4 py-2 text-white bg-blue-500 rounded hover:bg-blue-600 transition"
                            >
                                Show All
                            </Link>
                        </div>
                    </>
                )}
                {(isSigned && userRole === "ROLE_SU" || userRole === "ROLE_ADMIN") && (
                    <>
                        <NoteLogo linkTo={'/admin/home'} />
                        <div className="hidden md:flex space-x-4">
                            <Link
                                to="/admin/add-user"
                                className="px-4 py-2 text-white bg-blue-500 rounded hover:bg-blue-600 transition"
                            >
                                Add User
                            </Link>
                            <Link
                                to="/admin/all-users"
                                className="px-4 py-2 text-white bg-blue-500 rounded hover:bg-blue-600 transition"
                            >
                                Show Users
                            </Link>
                            <Link
                                to="/admin/all-notes"
                                className="px-4 py-2 text-white bg-blue-500 rounded hover:bg-blue-600 transition"
                            >
                                Show All Notes
                            </Link>
                        </div>
                    </>
                )}
            </div>

            {/* Right section: Profile / Sign In / Sign Out */}
            <div className="flex items-center space-x-4">
                {/* Desktop links */}
                <div className="hidden md:flex items-center space-x-4">


                    {isSigned ? (
                        <>
                            {userRole === "ROLE_USER" || userRole === "ROLE_GUEST" ? (
                                <>
                                    <Link
                                        to="/user/profile"
                                        className="px-4 py-2 text-gray-800 border border-gray-300 rounded hover:bg-gray-200 transition"
                                    >
                                        Profile
                                    </Link>
                                    <button
                                        onClick={handleSignOut}
                                        className="px-4 py-2 text-white bg-red-500 rounded hover:bg-red-600 transition"
                                    >
                                        Sign Out
                                    </button>
                                </>
                            ) : <>
                                <Link
                                    to="/admin/profile"
                                    className="px-4 py-2 text-gray-800 border border-gray-300 rounded hover:bg-gray-200 transition"
                                >
                                    Profile
                                </Link>
                                <button
                                    onClick={handleSignOut}
                                    className="px-4 py-2 text-white bg-red-500 rounded hover:bg-red-600 transition"
                                >
                                    Sign Out
                                </button>
                            </>}

                        </>
                    ) : (
                        <>
                            <Link
                                to="/signin"
                                className="px-4 py-2 text-white bg-blue-500 rounded hover:bg-blue-600 transition"
                            >
                                Sign In
                            </Link>
                            <Link
                                to="/signup"
                                className="px-4 py-2 text-white bg-blue-500 rounded hover:bg-blue-600 transition"
                            >
                                Sign Up
                            </Link>
                        </>
                    )}



                </div>

                {/* Hamburger menu icon for mobile */}
                <div className="md:hidden">
                    <button onClick={toggleMenu} className="focus:outline-none">
                        <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M4 6h16M4 12h16M4 18h16"></path>
                        </svg>
                    </button>
                </div>
            </div>

            {/* Mobile dropdown menu */}
            {isMenuOpen && (
                <div className="md:hidden absolute right-4 top-16 bg-white border border-gray-300 rounded-lg shadow-lg p-4 z-50">
                    {isSigned ? (
                        <>
                            <Link
                                to="/user/add-note"
                                className="block px-4 py-2 text-blue-500 hover:bg-gray-100 transition"
                                onClick={() => setIsMenuOpen(false)}
                            >
                                Add Note
                            </Link>
                            <Link
                                to="/user/all-notes"
                                className="block px-4 py-2 text-blue-500 hover:bg-gray-100 transition"
                                onClick={() => setIsMenuOpen(false)}
                            >
                                Show All
                            </Link>
                            <Link
                                to="/user/profile"
                                className="block px-4 py-2 text-gray-800 hover:bg-gray-100 transition"
                                onClick={() => setIsMenuOpen(false)}
                            >
                                Profile
                            </Link>
                            <button
                                onClick={() => { handleSignOut; setIsMenuOpen(false); }}
                                className="block w-full text-left px-4 py-2 text-red-500 hover:bg-gray-100 transition"
                            >
                                Sign Out
                            </button>
                        </>
                    ) : (
                        <>
                            <Link
                                to="/signin"
                                className="block px-4 py-2 text-blue-500 hover:bg-gray-100 transition"
                                onClick={() => setIsMenuOpen(false)}
                            >
                                Sign In
                            </Link>
                            <Link
                                to="/signup"
                                className="block px-4 py-2 text-blue-500 hover:bg-gray-100 transition"
                                onClick={() => setIsMenuOpen(false)}
                            >
                                Sign Up
                            </Link>
                        </>
                    )}
                </div>
            )}
        </header>
    );
};

export default Header;
