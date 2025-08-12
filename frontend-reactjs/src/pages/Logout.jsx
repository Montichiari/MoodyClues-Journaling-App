import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

import React from 'react'

const Logout = () => {
    const navigate = useNavigate();

    useEffect(() => {

        (async () => {
            try {
                await axios.get(`http://122.248.243.60:8080/api/user/logout`, {
                    withCredentials: true,
                    timeout: 8000
                });
            } catch (e) {
                console.log(e);
            } finally {
                    navigate('/login', { replace: true });
            }
        })();

    }, [navigate]);


    return (
        <div>Logout</div>
    )
}
export default Logout
