import React from 'react'
import { Link } from 'react-router-dom'

export default function NoteLogo({ linkTo }) {
    return (
        <Link to={linkTo} className="flex items-center space-x-3">
            {/* Logo SVG */}
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 50 50" className="w-8 h-8">
                <rect x="5" y="5" width="40" height="40" rx="5" ry="5" fill="#F9F9F9" stroke="#333" strokeWidth="2" />
                <line x1="8" y1="12" x2="42" y2="12" stroke="#333" strokeWidth="2" />
                <line x1="8" y1="18" x2="42" y2="18" stroke="#333" strokeWidth="2" />
                <line x1="8" y1="24" x2="42" y2="24" stroke="#333" strokeWidth="2" />
                <line x1="8" y1="30" x2="42" y2="30" stroke="#333" strokeWidth="2" />
                <line x1="8" y1="36" x2="42" y2="36" stroke="#333" strokeWidth="2" />
                <path d="M12 36 L18 30 L20 32 L14 38 Z" fill="#FFDA44" />
                <line x1="14" y1="38" x2="20" y2="32" stroke="#FFDA44" strokeWidth="2" />
            </svg>
            <span className="text-2xl font-poppins font-semibold text-gray-800">NoteMaster</span>
        </Link>

    )
}
