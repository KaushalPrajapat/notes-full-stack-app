import React, { useEffect } from 'react'
import { useLocation, useNavigate } from 'react-router-dom'
import { jwtDecode } from 'jwt-decode';
import AuthService from '../../services/AuthService';
export default function OAuth2RedirectHandler() {
    const navigate = useNavigate()
    const location = useLocation()


    useEffect(() => {
        loginOauth2();
        window.location.reload();
    }, [location])


    const loginOauth2 = async () => {
        const params = new URLSearchParams(location.search);
        const token = params.get('token');
        const refreshToken = params.get('refreshToken');
        if (token) {
            try {
                const decodedToken = jwtDecode(token);
                console.log(decodedToken);

                const username = decodedToken.sub;
                const roles = decodedToken.roles.split(',');
                console.log(roles);

                await AuthService.setTokenOAuth2(token, refreshToken, username, "ROLE_USER");
                navigate('/user/all-notes');


            } catch (error) {
                console.error(error + "Cound not parse token");
                navigate('/signin')
            }
        } else {
            console.log("Token not in url, returing to login");
            navigate("/signin")
        }
    }

    return (
        <div>OAuth2RedirectHandler</div>
    )
}
