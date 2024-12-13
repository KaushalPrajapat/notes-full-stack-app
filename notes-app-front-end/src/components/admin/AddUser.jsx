import React, { useState } from 'react';
import AdminService from '../../services/AdminService';
import { toast, ToastContainer } from 'react-toastify';

const AddUser = () => {
  // Define state variables for form data
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [role, setRole] = useState('USER');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState(false);

  // Form submission handler
  const handleSubmit = async (e) => {
    e.preventDefault();

    // Basic validation
    if (!username || !email || !password || !confirmPassword) {
      setError('All fields are required.');
      setSuccess(false);
      return;
    }
    if (password !== confirmPassword) {
      setError('Passwords do not match.');
      setSuccess(false);
      return;
    }

    // Add User API CALL

    try {
      const resp = await AdminService.addAUser(username, email, password, role);
      // console.log(resp);

      if (resp != null) {
        setSuccess(true);
        setError("")
        toast("User " + username + " saved successfully!!")
        setTimeout(() => {
        }, 1500);

      }
    } catch (error) {
      console.error(error);
      toast(error.message)
      setError(error.message)
      setSuccess(false)
    } finally {
      setPassword("")
      setConfirmPassword("")
    }
  }
  return (
    <div className="min-h-screen bg-gray-100 flex justify-center items-center">
      <div className="bg-white p-8 rounded-lg shadow-lg w-full max-w-md">
        <h2 className="text-3xl font-semibold text-center text-gray-700 mb-6">Add User</h2>

        {/* Display success or error message */}
        {error && <h6 className="bg-red-100 text-1xl font-semibold text-center mb-6 text-red-700">{error}</h6>}

        {success && <div className="text-green-500 text-sm mb-4"> User Added SuccesFully </div>}

        <form onSubmit={handleSubmit}>
          <div className="mb-4">
            <label className="block text-gray-700" htmlFor="username">Username</label>
            <input
              type="text"
              id="username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              className="mt-2 p-3 w-full border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              placeholder="Enter username"
              required
            />
          </div>

          <div className="mb-4">
            <label className="block text-gray-700" htmlFor="email">Email</label>
            <input
              type="email"
              id="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="mt-2 p-3 w-full border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              placeholder="Enter email"
              required
            />
          </div>

          <div className="mb-4">
            <label className="block text-gray-700" htmlFor="password">Password</label>
            <input
              type="password"
              id="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="mt-2 p-3 w-full border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              placeholder="Enter password"
              required
            />
          </div>

          <div className="mb-4">
            <label className="block text-gray-700" htmlFor="confirmPassword">Confirm Password</label>
            <input
              type="password"
              id="confirmPassword"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
              className="mt-2 p-3 w-full border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              placeholder="Confirm password"
              required
            />
          </div>

          <div className="mb-6">
            <label className="block text-gray-700" htmlFor="role">Role</label>
            <select
              id="role"
              value={role}
              onChange={(e) => setRole(e.target.value)}
              className="mt-2 p-3 w-full border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
            >
              <option value="USER">User</option>
              <option value="ADMIN">Admin</option>
              <option value="GUEST">Guest</option>
            </select>
          </div>

          <button
            type="submit"
            className="w-full bg-blue-500 text-white py-3 rounded-md hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            Add User
          </button>
        </form>
      </div>
    </div>
  );
};

export default AddUser;
