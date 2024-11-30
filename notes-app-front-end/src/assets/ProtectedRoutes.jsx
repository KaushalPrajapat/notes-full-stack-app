import React from 'react'
import { Navigate, Outlet } from 'react-router-dom'

export default function ProtectedRoutes({ isSigned, userRole }) {
    // console.log(isSigned);

    return (
        isSigned && (userRole === "ROLE_USER" || userRole === "ROLE_GUEST") ? <Outlet /> : <Navigate to="/signin" />
    )
}
