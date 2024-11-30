import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import UserService from '../../services/UserService';

const Profile = ({ handleSignOut }) => {
  const [username, setUsername] = useState(null);
  const [role, setRole] = useState(null);
  const [isChangingPassword, setIsChangingPassword] = useState(false); // For toggling password fields
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [error, setError] = useState('');
  const [successMessage, setSuccessMessage] = useState('');
  const navigate = useNavigate();

  // Fetch username and role from localStorage
  useEffect(() => {
    const storedUsername = localStorage.getItem('username');
    const storedRole = localStorage.getItem('role');

    // If data exists in localStorage, set them to state
    if (storedUsername && storedRole) {
      setUsername(storedUsername);
      setRole(storedRole);
    } else {
      navigate("/signin");
    }
  }, [navigate]);

  // Handle Change Password button toggle
  const handleChangePasswordClick = () => {
    setIsChangingPassword(!isChangingPassword); // Toggle visibility of password fields
    setError(''); // Clear any error message when toggling
    setSuccessMessage(''); // Clear any success message when toggling
  };

  // Handle password change form submission
  const handlePasswordSubmit = async (e) => {
    e.preventDefault();
    if (localStorage.getItem("role") === "ROLE_SU") {
      alert("😡😡 No one chnage super user password 😡😡")
      return;
    }
    else {
      if (password !== confirmPassword) {
        setError('Passwords do not match.');
        return;
      }
      setError('');
      setSuccessMessage('');

      try {

        const response = await UserService.updatePassword(password);
        console.log(response);

        if (response.data.httpStatus === 200) {
          alert("Now SignIn again with your new password 🙃🙃 !")
          handleSignOut();
          setSuccessMessage('Password updated successfully!');
          setPassword('');
          setConfirmPassword('');
          setIsChangingPassword(false);
        } else {
          setError("Error changing password, Try again");
        }
        // Hide the }password fields after success
      } catch (error) {
        // Handle any error that occurs during the password update
        setError('Error updating password. Please try again later.');
      }
    }
  };

  // If profile data is not available (e.g., username or role), show message
  if (!username || !role) {
    return (
      <div className="text-center p-6">
        <h3 className="text-2xl font-semibold text-gray-700">No profile data found</h3>
        <p className="text-gray-500">Please login to view your profile.</p>
      </div>
    );
  }

  return (
    <div className="max-w-2xl mx-auto p-6 bg-white shadow-md rounded-lg">
      <h2 className="text-3xl font-semibold text-center mb-6">User Profile</h2>

      {/* Display Username */}
      <div className="text-center mb-6">
        <div className="text-lg font-medium text-gray-700">Username:</div>
        <div className="text-2xl font-semibold text-gray-900">{username}</div>
      </div>

      {/* Display Role */}
      <div className="text-center mb-6">
        <div className="text-lg font-medium text-gray-700">Role:</div>
        <div className="text-xl font-semibold text-gray-900">{role}</div>
      </div>


      {/* Toggle Change Password */}
      <div className="text-center">
        <button
          className="px-6 py-3 bg-blue-500 text-white font-semibold rounded-md hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-500"
          onClick={handleChangePasswordClick}
        >
          {isChangingPassword ? 'Cancel' : 'Change Password'}
        </button>
      </div>



      {/* Change Password Form (shown when isChangingPassword is true) */}
      {isChangingPassword && (
        <form onSubmit={handlePasswordSubmit} className="mt-6">
          <div className="mb-4">
            <label htmlFor="password" className="block text-lg font-medium text-gray-700">New Password</label>
            <input
              type="password"
              id="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="w-full p-3 mt-1 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
            />
          </div>

          <div className="mb-4">
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

          {/* Show error or success message */}
          {error && <p className="text-red-500 text-sm">{error}</p>}
          {successMessage && <p className="text-green-500 text-sm">{successMessage}</p>}

          {/* Submit Button */}
          <div className="text-center mt-6">
            <button
              type="submit"
              className="px-6 py-3 bg-blue-500 text-white font-semibold rounded-md hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              Update Password
            </button>
          </div>
        </form>
      )}
    </div>
  );
};

export default Profile;
