import React from 'react';
import { Box, Toolbar } from '@mui/material';
import Sidenav from './components/Sidenav';
import { Outlet } from 'react-router-dom';

const drawerWidth = 240;

const AppLayout = () => {
    return (
        <Box sx={{ display: 'flex' }}>
            <Sidenav />
            <Box
                component="main"
                sx={{ flexGrow: 1, bgcolor: 'background.default', p: 3, ml: `${drawerWidth}px` }}
            >
                <Toolbar />
                <Outlet />
            </Box>
        </Box>
    );
};

export default AppLayout;
