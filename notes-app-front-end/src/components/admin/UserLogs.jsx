import React, { useState, useEffect } from 'react';
import AdminService from '../../services/AdminService';
import BounceLoader from "react-spinners/ClipLoader";

const UserLogs = () => {
  const [logs, setLogs] = useState([]);      // State to store the logs data
  const [isLoading, setIsLoading] = useState(true);  // Loading state
  const [error, setError] = useState('');     // Error state for handling failed requests

  // Fetch logs from the backend
  useEffect(() => {
    const fetchLogs = async () => {
      try {
        const response = await AdminService.getAllUserLogs();  // Replace with your actual API endpoint
        console.log(response);
        
        if (response.status!=200) {
          throw new Error('Failed to fetch logs');
        }
        setLogs(response.data);  // Update the logs state with the fetched data
        setIsLoading(false);  // Set loading to false after fetching is complete
      } catch (error) {
        setError(error.message);  // Handle any errors that occurred during fetch
        setIsLoading(false);
      }
    };

    fetchLogs();  // Call the fetchLogs function when the component mounts
  }, []); // Empty dependency array ensures this runs only once when the component mounts

  // Return the component UI
  return (
    <div className="max-w-6xl mx-auto p-6 bg-white shadow-md rounded-lg">
      <h1 className="text-2xl font-semibold mb-4">User Operation Logs</h1>

      {/* Display loading message */}
      {isLoading && <div className="text-3xl font-semibold text-center mb-6"><BounceLoader
            color="hsla(217, 19%, 52%, 1)"
            cssOverride={{}}
            size={60}
            speedMultiplier={5}
        /></div>}

      {/* Display error message if fetching fails */}
      {error && <div className="bg-red-100 text-red-700 p-2 rounded mb-4">{error}</div>}

      {/* Check if there are no logs */}
      {logs.length === 0 && !isLoading && !error && <p>No logs available.</p>}

      {/* Table to display logs */}
      {!isLoading && !error && logs.length > 0 && (
        <table className="min-w-full bg-white border border-gray-300 rounded-lg">
          <thead>
            <tr>
              <th className="py-2 px-4 text-left border-b">Log ID</th>
              <th className="py-2 px-4 text-left border-b">Operation</th>
              <th className="py-2 px-4 text-left border-b">Changed By</th>
              <th className="py-2 px-4 text-left border-b">On(UserId)</th>
              <th className="py-2 px-4 text-left border-b">Updated Date</th>
            </tr>
          </thead>
          <tbody>
            {logs.map((log) => (
              <tr key={log.userLogId}>
                <td className="py-2 px-4 border-b">{log.userLogId}</td>
                <td className="py-2 px-4 border-b">{log.operation}</td>
                <td className="py-2 px-4 border-b">{log.changedBy}</td>
                <td className="py-2 px-4 border-b">{log.changedOn}</td>
                <td className="py-2 px-4 border-b">{log.updatedDate}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default UserLogs;
