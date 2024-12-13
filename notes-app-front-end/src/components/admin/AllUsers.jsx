import React, { useState, useEffect } from "react";
import AdminService from "../../services/AdminService";
import BounceLoader from "react-spinners/ClipLoader";
import { MdOutlineMailOutline } from "react-icons/md";
const AllUsers = () => {
  // State to hold user data, including password (for this example)
  const [users, setUsers] = useState([]);
  const [enable, setEnable] = useState();
  // Loading state
  const [isLoading, setIsLoading] = useState(false);

  // Error state
  const [error, setError] = useState("");

  // State for tracking the user whose password is being edited
  const [editingPasswordForUserId, setEditingPasswordForUserId] = useState(null);

  // Password field state
  const [newPassword, setNewPassword] = useState("");

  // Simulate fetching user data (replace with actual API)
  useEffect(() => {
    const fetchUserData = async () => {
      try {
        setIsLoading(true);
        const response = await AdminService.getAllUsers(); // Replace with your actual API
        setUsers(response.data);
        setIsLoading(false);
      } catch (err) {
        setError(err.message);
        setIsLoading(false);
      }
    };

    fetchUserData();
  }, []);

  // Handle toggle (enabled / twoFactorEnabled)
  const handleToggle = async (userId, field) => {
    try {
      if (userId == 1) {
        setError("😡😡 Bhai, Super User ka Status change nahi hota 😡😡")
        return;
      }
      setUsers((prevState) =>
        prevState.map((user) =>
          user.userId === userId ? { ...user, [field]: !user[field] } : user
        )
      );
      setIsLoading(true);
      console.log(userId, field);
      users.map(async (user) => {
        if (user.userId == userId) {
          console.log(user.enabled);
          try {
            const response = await AdminService.updateEnabledStatus(userId, !user.enabled)
            console.log(response);
            if (response.data.httpStatus == 200) {
              if (user.enabled == true)
                alert("User Status changed to disabled")
              else
                alert("User Status changed to enabled")
            } else {
              alert(response.data.message)
            }
            // Reset loading state
            setIsLoading(false);
          } catch (err) {
            setUsers((prevState) =>
              prevState.map((user) =>
                user.userId === userId ? { ...user, [field]: !user[field] } : user
              )
            );
            setError(err.message)
            setIsLoading(false)
          }
        }
      }
      )
    } catch (err) {
      setError(err.message);
      // Reset toggle if the update fails
      setUsers((prevState) =>
        prevState.map((user) =>
          user.userId === userId ? { ...user, [field]: !user[field] } : user
        )
      );
      setIsLoading(false);
      setError("")
    }
  };

  // Handle password change request
  const handlePasswordChange = async (userId) => {
    try {
      if (userId == 1) {
        alert("😡😡 Bhai, Super User ka Status change nahi hota 😡😡")
        setError("😡😡 Bhai, Super User ka Status change nahi hota 😡😡")
        SetTimeIn(() => {
          setError("")
        }, 2000);
        setError("")
        return;
      }
      if (!newPassword) {
        alert("Please enter a new password");
        return;
      }
      alert("Updating password to : id " + userId);
      setIsLoading(true);

      // Send the new password to the backend API (replace with your actual endpoint)
      const response = await AdminService.updatePassword(userId, newPassword);
      console.log(response.data.httpStatus);
      if (response.data.httpStatus == 200) {
        console.log('changed');

        alert("Password changed successfully");
      }
      // Reset loading state and password input
      setIsLoading(false);
      setEditingPasswordForUserId(null); // Reset editing state
      setNewPassword(""); // Clear the password input
    } catch (err) {
      setError("Failed to update password.");
      setIsLoading(false);
    }
  };

  return (
    <div className="max-w-6xl mx-auto p-6 bg-white shadow-md rounded-lg">
      <h1 className="text-3xl font-semibold text-center mb-6">User Dashboard</h1>

      {/* Error Message */}
      {error && <h6 className="text-1xl bg-red-100 font-semibold text-center mb-6 text-red-700">{error}</h6>}


      {/* Loading state */}
      {isLoading ? (
        <div className="text-3xl font-semibold text-center mb-6"><BounceLoader
          color="hsla(217, 19%, 52%, 1)"
          cssOverride={{}}
          size={60}
          speedMultiplier={5}
        /></div>
      ) : (
        <div className="space-y-4">
          {users.map((user) => (
            <div key={user.userId} className="bg-gray-50 p-4 rounded-lg shadow-sm">
              <div className="flex justify-between items-center my-1">
                <div>
                  <p><strong>Username:</strong> {user.username}</p>
                  <p className="flex items-center"><MdOutlineMailOutline className="mr-2"/> {user.email}</p>
                  <p><strong>Role:</strong> {user.role.roleName}</p>
                  <p><strong>Account Created:</strong> {user.createdDate}</p>
                  <p><strong>Account Expiry:</strong> {user.accountExpiryDate}</p>

                  {/* Password Field */}
                  <div className="mt-4">
                    <div className="flex items-center">
                      <label htmlFor={`password-${user.userId}`} className="font-medium mr-2">Password:</label>
                      <input
                        id={`password-${user.userId}`}
                        type={editingPasswordForUserId === user.userId ? "text" : "password"}
                        value={editingPasswordForUserId === user.userId ? newPassword : "***"}
                        onChange={(e) => setNewPassword(e.target.value)}
                        disabled={editingPasswordForUserId !== user.userId}
                        className="px-4 py-2 rounded-md border border-gray-300"
                      />
                    </div>

                    <button
                      className="mt-2 text-blue-500 hover:underline"
                      onClick={() => setEditingPasswordForUserId(user.userId)} // Set the user as editing
                    >
                      Change Password
                    </button>

                    {editingPasswordForUserId === user.userId && (
                      <button
                        className="ml-4 text-green-500 hover:underline"
                        onClick={() => handlePasswordChange(user.userId)}
                      >
                        Save New Password
                      </button>
                    )}
                  </div>
                </div>

                <div className="space-y-4">
                  {/* Account Enabled Toggle */}
                  <div className="flex items-center space-x-3">
                    <label htmlFor={`enabled-${user.userId}`} className="font-medium">Account Status:</label>
                    <button
                      id={`enabled-${user.userId}`}
                      onClick={() => handleToggle(user.userId, "enabled")}
                      className={`px-4 py-2 rounded-full transition-colors ${user.enabled ? "bg-green-500" : "bg-gray-300"
                        } text-white ${isLoading ? "opacity-50 cursor-not-allowed" : ""}`}
                      disabled={isLoading}
                    >
                      {isLoading ? "Updating..." : user.enabled ? "Active" : "Inactive"}
                    </button>
                  </div>

                  {/* Two Factor Authentication Toggle */}
                  <div className="flex items-center space-x-3">
                    <label htmlFor={`twoFactorEnabled-${user.userId}`} className="font-medium">2FA Enabled:</label>
                    <button
                      id={`twoFactorEnabled-${user.userId}`}
                      onClick={() => handleToggle(user.userId, "twoFactorEnabled")}
                      className={`px-4 py-2 rounded-full transition-colors ${user.twoFactorEnabled ? "bg-blue-500" : "bg-gray-300"
                        } text-white ${isLoading ? "opacity-50 cursor-not-allowed" : ""}`}
                      disabled={isLoading}
                    >
                      {isLoading ? "Updating..." : user.twoFactorEnabled ? "Active" : "Inactive"}
                    </button>
                  </div>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default AllUsers;
