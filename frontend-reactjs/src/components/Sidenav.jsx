import { Drawer, List, ListItemButton, ListItemText, Toolbar } from '@mui/material';
import { Link, useLocation } from 'react-router-dom';
import logo from '../assets/moodyclues-logo.png';

const drawerWidth = 200;
const userId = localStorage.getItem("userId");

const navItems = [
    { label: 'Home', path: '/home' },
    { label: 'Write Journal', path: '/journal/mood' },
    { label: 'Submit habits', path: '/journal/habits' },
    { label: 'Read Journal', path: '/read' },
    { label: 'Dashboard', path: '/dashboard' },
    { label: 'Link Requests', path: '/invites' },
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
        </>
    );

    return (
        <>
            {isMobile &&  (
                <AppBar
                    position="fixed"
                    color="transparent"
                    sx={{
                        zIndex: (theme) => theme.zIndex.appBar,
                        bgcolor: 'rgba(31,31,31,0.78)',
                        backdropFilter: 'blur(6px)',
                        boxShadow: '0 4px 12px rgba(0,0,0,0.25)'
                    }}
                >
                    <Toolbar sx={{ px: 1, py: 0.5 }}>
                        <IconButton color="inherit" edge="start" onClick={handleDrawerToggle} sx={{ mr: 1 }}>
                            <MenuIcon />
                        </IconButton>
                    </Toolbar>
                </AppBar>
            )}

            {isMobile && <Toolbar sx={{ mb: 2 }} />}

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
                        ...(isMobile && {
                            zIndex: (theme) => theme.zIndex.appBar + 1, // Drawer above AppBar on mobile
                            backgroundColor: 'rgba(255,255,255,0.95)',
                            backdropFilter: 'blur(4px)'
                        })
                    }
                }}
            >
                {drawerContent}
            </Drawer>

        </>
    );
};

export default Sidenav;
