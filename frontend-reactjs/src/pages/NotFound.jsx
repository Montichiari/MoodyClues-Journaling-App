import { Box, Toolbar } from '@mui/material';
import Sidenav from '../components/Sidenav'; // adjust path if needed
import { Link, useNavigate } from 'react-router-dom';
import React from 'react';

const NotFound = () => {
    const navigate = useNavigate();

    return (
        <Box sx={{ display: 'flex' }}>
            <Box
                component="main"
                sx={{ flexGrow: 1, p: 3  }}
                className="px-16 py-10"
            >
                <Toolbar />
                <section className="max-w-2xl">
                    <h1 className="text-4xl font-extrabold mb-2">404</h1>
                    <p className="text-lg text-stone-600 mb-6">Page not found.</p>
                </section>
            </Box>
        </Box>
    );
};

export default NotFound;
