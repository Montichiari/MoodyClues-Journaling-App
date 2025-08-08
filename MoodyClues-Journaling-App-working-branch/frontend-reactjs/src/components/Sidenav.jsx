import { useState } from 'react';
import {
    Drawer,
    List,
    ListItemButton,
    ListItemText,
    Toolbar,
    IconButton,
    AppBar,
    useMediaQuery
} from '@mui/material';
import MenuIcon from '@mui/icons-material/Menu';
import { Link, useLocation } from 'react-router-dom';
import logo from '../assets/moodyclues-logo.png';
import { useTheme } from '@mui/material/styles';

const drawerWidth = 200;

const navItems = [
    { label: 'Home', path: '/home' },
    { label: 'Write', path: '/journal/mood' },
    { label: 'Read', path: '/read' },
    { label: 'Dashboards', path: '/dashboard' },
    { label: 'Invites', path: '/invites' },
    { label: 'Edit Profile', path: '/profile' },
    { label: 'Logout', path: '/logout' }
];

const Sidenav = () => {
    const location = useLocation();
    const theme = useTheme();
    const isMobile = useMediaQuery(theme.breakpoints.down('md'));

    const [mobileOpen, setMobileOpen] = useState(false);

    const handleDrawerToggle = () => {
        setMobileOpen(!mobileOpen);
    };

    const drawerContent = (
        <>
            <Toolbar className="flex justify-center items-center h-16">
                <img src={logo} alt="MoodyClues" className="w-32 h-auto mx-auto my-4" />
            </Toolbar>
            <List>
                {navItems.map(({ label, path }) => (
                    <ListItemButton
                        key={path}
                        component={Link}
                        to={path}
                        selected={location.pathname === path}
                        onClick={() => isMobile && setMobileOpen(false)}
                    >
                        <ListItemText primary={label} />
                    </ListItemButton>
                ))}
            </List>
        </>
    );

    return (
        <>
            {isMobile && (
                <AppBar position="fixed" sx={{ zIndex: (theme) => theme.zIndex.drawer + 1 }}>
                    <Toolbar>
                        <IconButton
                            color="inherit"
                            edge="start"
                            onClick={handleDrawerToggle}
                        >
                            <MenuIcon />
                        </IconButton>
                    </Toolbar>
                </AppBar>
            )}

            <Drawer
                variant={isMobile ? 'temporary' : 'permanent'}
                open={isMobile ? mobileOpen : true}
                onClose={handleDrawerToggle}
                ModalProps={{ keepMounted: true }}
                sx={{
                    width: drawerWidth,
                    flexShrink: 0,
                    '& .MuiDrawer-paper': {
                        width: drawerWidth,
                        boxSizing: 'border-box',
                    }
                }}
            >
                {drawerContent}
            </Drawer>
        </>
    );
};

export default Sidenav;
