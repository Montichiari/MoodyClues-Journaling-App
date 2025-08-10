import { Drawer, List, ListItemButton, ListItemText, Toolbar } from '@mui/material';
import { Link, useLocation } from 'react-router-dom';
import logo from '../assets/moodyclues-logo.png';

const drawerWidth = 200;

const navItems = [
    { label: 'Home', path: '/' },
    { label: 'Write', path: '/journal/mood' },
    { label: 'Read', path: '/journal/read' },
    { label: 'Dashboards', path: '/dashboards' },
    { label: 'Invites', path: '/invites' },
    { label: 'Edit Profile', path: '/profile' },
    { label: 'Logout', path: '/logout' }
];

const Sidenav = () => {
    const location = useLocation();

    return (
        <Drawer
            variant="permanent"
            sx={{
                width: drawerWidth,
                flexShrink: 0,
                '& .MuiDrawer-paper': {
                    width: drawerWidth,
                    boxSizing: 'border-box',
                }
            }}
        >
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
                    >
                        <ListItemText primary={label} />
                    </ListItemButton>
                ))}
            </List>
        </Drawer>
    );
};

export default Sidenav;
