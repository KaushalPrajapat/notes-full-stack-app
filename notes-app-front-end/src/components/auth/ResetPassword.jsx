import React, { useState } from 'react'
import AuthService from "../../services/AuthService";
import { toast, ToastContainer } from "react-toastify";
import { useSearchParams } from 'react-router-dom';


export default function ResetPassword() {
    const [newPassword, setNewPassword] = useState("");
    const [message, setMessage] = useState("");
    const [searchParams] = useSearchParams();

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            // console.log(newPassword);
            const token = searchParams.get('token')
            // console.log(searchParams);
            // console.log(token);

            const response = await AuthService.resetPassword(newPassword, token);
            // console.log(response);

            if (response.data.httpStatus == 200) {
                toast(response.data.message)
                setMessage(response.data.message);
            } else {
                toast("Failed to Reset password.");
                setMessage(errorData.message || "Failed to Reset Password.");
            }
        } catch (error) {
            setMessage("An error occurred. Please try again.");
        }
    };

    return (
        <div className="flex items-center justify-center h-screen bg-gray-100">
            <form
                className="bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4 w-96"
                onSubmit={handleSubmit}
            >
                <h2 className="text-xl font-bold text-center mb-4">Forgot Password</h2>
                <div className="mb-4">
                    <label
                        htmlFor="newPassword"
                        className="block text-gray-700 text-sm font-bold mb-2"
                    >
                        new Password
                    </label>
                    <input
                        type="password"
                        id="newPassword"
                        name="newPassword"
                        value={newPassword}
                        onChange={(e) => setNewPassword(e.target.value)}
                        required
                        className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                        placeholder="Enter your new Password"
                    />
                </div>
                <button
                    type="submit"
                    className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline w-full"
                >
                    Submit
                </button>
                {message && (
                    <p className="text-center text-sm mt-4 text-red-600">{message}</p>
                )}
            </form>
        </div>
    );
}
