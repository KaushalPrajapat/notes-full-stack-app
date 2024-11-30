import React from 'react';
import { Link } from 'react-router-dom';

const AdminHome = ({ isSuperUser }) => {
  return (
    <div className="min-h-screen bg-gray-50 p-8">
      {/* Header Section */}
      <div className="flex justify-between items-center mb-8">
        <h1 className="text-4xl font-semibold text-gray-800">Admin Dashboard</h1>
      </div>

      {/* Admin Capabilities Section */}
      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6">
        {/* Add User */}
        <div className="bg-white p-6 rounded-lg shadow-lg hover:shadow-xl transition-shadow duration-200">
          <div className="flex items-center space-x-4">
            <span className="text-4xl text-blue-500">➕</span>
            <div>
              <h2 className="text-2xl font-semibold text-gray-800">
                <Link to="/admin/add-user" className="hover:text-blue-600">Add User</Link>
              </h2>
              <p className="text-gray-600">Quickly add a new user to the system.</p>
            </div>
          </div>
        </div>

        {/* View All Users */}
        <div className="bg-white p-6 rounded-lg shadow-lg hover:shadow-xl transition-shadow duration-200">
          <div className="flex items-center space-x-4">
            <span className="text-4xl text-green-500">👁️</span>
            <div>
              <h2 className="text-2xl font-semibold text-gray-800">
                <Link to="/admin/all-users" className="hover:text-green-600">View All Users</Link>
              </h2>
              <p className="text-gray-600">See the list of all users in the system.</p>
            </div>
          </div>
        </div>
        {isSuperUser && (
          <>
            {/* Delete User */}
            <div div className="bg-white p-6 rounded-lg shadow-lg hover:shadow-xl transition-shadow duration-200">
              <div className="flex items-center space-x-4">
                <span className="text-4xl text-red-500">🗑️</span>
                <div>
                  <h2 className="text-2xl font-semibold text-gray-800">
                    <a href="/admin/su/user-logs" className="text-gray-600 hover:text-red-600">User Logs</a>
                  </h2>
                  <p className="text-gray-600">View all logs over a user.</p>
                </div>
              </div>
            </div>
          </>
        )
        }
        {/* All Notes */}
        <div className="bg-white p-6 rounded-lg shadow-lg hover:shadow-xl transition-shadow duration-200">
          <div className="flex items-center space-x-4">
            <span className="text-4xl text-purple-500">📝</span>
            <div>
              <h2 className="text-2xl font-semibold text-gray-800">
                <Link to="/admin/all-notes" className="hover:text-purple-600">All Notes</Link>
              </h2>
              <p className="text-gray-600">View and manage all notes in the system.</p>
            </div>
          </div>
        </div>


        {/* Update Password */}
        <div className="bg-white p-6 rounded-lg shadow-lg hover:shadow-xl transition-shadow duration-200">
          <div className="flex items-center space-x-4">
            <span className="text-4xl text-yellow-500">🔑</span>
            <div>
              <h2 className="text-2xl font-semibold text-gray-800">
                <a href="/admin/all-notes-logs" className="text-gray-600 hover:text-yellow-600">Notes Logs</a>
              </h2>
              <p className="text-gray-600">View Logs of All notes for all users.</p>
            </div>
          </div>
        </div>

        {/* Change Account Status */}
        <div className="bg-white p-6 rounded-lg shadow-lg hover:shadow-xl transition-shadow duration-200">
          <div className="flex items-center space-x-4">
            <span className="text-4xl text-indigo-500">⚙️</span>
            <div>
              <h2 className="text-2xl font-semibold text-gray-800">
                <a href="#" className="text-gray-600 hover:text-indigo-600">Change Account Status</a>
              </h2>
              <p className="text-gray-600">Enable or disable user accounts.</p>
            </div>
          </div>
        </div>


      </div>
    </div >
  );
}

export default AdminHome;
