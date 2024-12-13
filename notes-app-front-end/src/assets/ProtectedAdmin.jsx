import React from 'react'
import { Navigate, Outlet } from 'react-router-dom'

export default function ProtectedAdmin({ isSigned, userRole }) {
    // console.log(isSigned);
    // console.log(isSigned && (userRole === "ROLE_SU" || userRole === "ROLE_ADMIN") + 'cjhing for defalt route');

    return (
        isSigned && (userRole === "ROLE_SU" || userRole === "ROLE_ADMIN") ? <Outlet /> : <Navigate to="/signin" />
    )
}
